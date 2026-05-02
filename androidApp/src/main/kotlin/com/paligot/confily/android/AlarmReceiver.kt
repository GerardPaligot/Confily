package com.paligot.confily.android

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null) {
            showReminder(context, intent)
        }
    }

    private fun showReminder(context: Context, intent: Intent) {
        val id = intent.getStringExtra(AlarmIntentFactoryImpl.ID)
        val title = intent.getStringExtra(AlarmIntentFactoryImpl.TITLE)
        val text = intent.getStringExtra(AlarmIntentFactoryImpl.TEXT)
        if (id == null || title == null || text == null) return
        val notificationManager = NotificationManagerCompat.from(context)
        if (!notificationManager.areNotificationsEnabled()) return
        val notification = NotificationCompat.Builder(context, AlarmIntentFactoryImpl.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_campaign)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    MainActivity.create(context),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setAutoCancel(true)
            .build()
        notificationManager.notify(id.hashCode(), notification)
    }
}
