package org.gdglille.devfest.android

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.android.data.AlarmIntentFactory

object AlarmIntentFactoryImpl : AlarmIntentFactory {
    const val CHANNEL_ID = "alarm.notification"
    const val ID = "alarm.id"
    const val TITLE = "alarm.title"
    const val TEXT = "alarm.text"

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun create(context: Context, id: String, title: String, text: String): Intent =
        Intent(context, AlarmReceiver::class.java).apply {
            putExtra(ID, id)
            putExtra(TITLE, title)
            putExtra(TEXT, text)
        }
}
