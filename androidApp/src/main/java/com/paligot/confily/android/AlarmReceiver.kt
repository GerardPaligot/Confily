package com.paligot.confily.android

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@Suppress("DEPRECATION")
@FlowPreview
@ExperimentalCoroutinesApi
class AlarmReceiver : BroadcastReceiver() {
    @OptIn(ExperimentalSettingsApi::class)
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return
        val id = intent.extras!![AlarmIntentFactoryImpl.ID] as String
        val title = intent.extras!![AlarmIntentFactoryImpl.TITLE] as String
        val text = intent.extras!![AlarmIntentFactoryImpl.TEXT] as String
        val builder = NotificationCompat.Builder(context, AlarmIntentFactoryImpl.CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_campaign)
            setContentTitle(title)
            setContentText(text)
            setStyle(NotificationCompat.BigTextStyle().bigText(text))
            priority = NotificationCompat.PRIORITY_DEFAULT
            setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    MainActivity.create(context),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            setAutoCancel(true)
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(AlarmIntentFactoryImpl.CHANNEL_ID, "Devfest Lille App", importance)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(id.hashCode(), builder.build())
    }
}
