package org.gdglille.devfest.android.data

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.SystemClock
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.models.TalkItemUi
import org.gdglille.devfest.repositories.AgendaRepository
import java.util.Locale

private const val ReminderInMinutes = 10

class AlarmScheduler(
    private val repository: AgendaRepository,
    private val alarmManager: AlarmManager,
    private val alarmIntentFactory: AlarmIntentFactory
) {
    @SuppressLint("UnspecifiedImmutableFlag")
    suspend fun schedule(context: Context, talkItem: TalkItemUi) = coroutineScope {
        val isFavorite = !talkItem.isFavorite
        repository.markAsRead(talkItem.id, isFavorite)
        val title = context.getString(
            R.string.title_notif_reminder_talk, talkItem.room.lowercase(Locale.getDefault())
        )
        val intent = alarmIntentFactory.create(context, talkItem.id, title, talkItem.title)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent =
            PendingIntent.getBroadcast(context, talkItem.id.hashCode(), intent, flags)
        if (isFavorite) {
            val time =
                talkItem.startTime.toLocalDateTime().toInstant(TimeZone.currentSystemDefault())
                    .minus(ReminderInMinutes, DateTimeUnit.MINUTE).toEpochMilliseconds()
            alarmManager.set(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + (time - Clock.System.now().toEpochMilliseconds()),
                pendingIntent
            )
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }
}
