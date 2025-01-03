package com.paligot.confily.core

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.SystemClock
import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.title_notif_reminder_talk
import com.paligot.confily.schedules.ui.models.TalkItemUi
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.getString
import java.util.Locale

actual class AlarmScheduler(
    private val context: Context,
    private val repository: SessionRepository,
    private val alarmManager: AlarmManager,
    private val alarmIntentFactory: AlarmIntentFactory
) {
    @SuppressLint("UnspecifiedImmutableFlag")
    actual suspend fun schedule(talkItem: TalkItemUi) = coroutineScope {
        val isFavorite = !talkItem.isFavorite
        repository.markAsRead(talkItem.id, isFavorite)
        val title = getString(
            Resource.string.title_notif_reminder_talk,
            talkItem.room.lowercase(Locale.getDefault())
        )
        val intent = alarmIntentFactory.create(context, talkItem.id, title, talkItem.title)
        val flags =
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent =
            PendingIntent.getBroadcast(context, talkItem.id.hashCode(), intent, flags)
        if (isFavorite) {
            val time =
                talkItem.startTime.toLocalDateTime().toInstant(TimeZone.UTC)
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

    companion object {
        private const val ReminderInMinutes = 10
    }
}
