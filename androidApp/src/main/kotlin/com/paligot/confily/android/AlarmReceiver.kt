package com.paligot.confily.android

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return
        val id = intent.getStringExtra(AlarmIntentFactoryImpl.ID) ?: return
        val title = intent.getStringExtra(AlarmIntentFactoryImpl.TITLE) ?: return
        val text = intent.getStringExtra(AlarmIntentFactoryImpl.TEXT) ?: return
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
        val notificationManager = NotificationManagerCompat.from(context)
        if (!notificationManager.areNotificationsEnabled()) return
        notificationManager.notify(id.hashCode(), notification)
    }
}
