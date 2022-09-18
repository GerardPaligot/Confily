package org.gdglille.devfest.android

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.NotificationCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@ExperimentalSettingsApi
@FlowPreview
@ExperimentalMaterial3Api
@ExperimentalPermissionsApi
class AlarmReceiver : BroadcastReceiver() {
    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return
        val id = intent.extras!![ID] as String
        val title = intent.extras!![TITLE] as String
        val text = intent.extras!![TEXT] as String
        val builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(title)
            setContentText(text)
            setStyle(NotificationCompat.BigTextStyle().bigText(text))
            priority = NotificationCompat.PRIORITY_DEFAULT
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
            setContentIntent(PendingIntent.getActivity(context, 0, MainActivity.create(context), flags))
            setAutoCancel(true)
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, "Devfest Lille App", importance)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(id.hashCode(), builder.build())
    }

    companion object {
        private const val CHANNEL_ID = "alarm.notification"
        private const val ID = "alarm.id"
        private const val TITLE = "alarm.title"
        private const val TEXT = "alarm.text"
        fun create(context: Context, id: String, title: String, text: String): Intent =
            Intent(context, AlarmReceiver::class.java).apply {
                putExtra(ID, id)
                putExtra(TITLE, title)
                putExtra(TEXT, text)
            }
    }
}
