package com.paligot.confily.core

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.title_notif_reminder_talk
import com.paligot.confily.schedules.ui.models.TalkItemUi
import kotlinx.coroutines.coroutineScope
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
    actual suspend fun schedule(talkItem: TalkItemUi) = coroutineScope {
        val isFavorite = !talkItem.isFavorite
        repository.markAsRead(talkItem.id, isFavorite)
        val title = getString(
            Resource.string.title_notif_reminder_talk,
            talkItem.room.lowercase(Locale.getDefault())
        )
        val intent = alarmIntentFactory.create(context, talkItem.id, title, talkItem.title)
        val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent =
            PendingIntent.getBroadcast(context, talkItem.id.hashCode(), intent, flags)
        if (isFavorite) {
            val triggerAtMillis = talkItem.startTime
                .toLocalDateTime()
                .toInstant(TimeZone.UTC)
                .minus(ReminderInMinutes, DateTimeUnit.MINUTE)
                .toEpochMilliseconds()
            if (canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } else {
            alarmManager.cancel(pendingIntent)
        }
    }

    private fun canScheduleExactAlarms(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()

    companion object {
        private const val ReminderInMinutes = 10
    }
}
