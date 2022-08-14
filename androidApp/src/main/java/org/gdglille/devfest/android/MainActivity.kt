package org.gdglille.devfest.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.stringResource
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.android.data.QrCodeGeneratorAndroid
import org.gdglille.devfest.android.theme.vitamin.features.Main
import org.gdglille.devfest.database.DatabaseWrapper
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.database.ScheduleDao
import org.gdglille.devfest.database.SpeakerDao
import org.gdglille.devfest.database.TalkDao
import org.gdglille.devfest.database.UserDao
import org.gdglille.devfest.network.ConferenceApi
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.SpeakerRepository
import org.gdglille.devfest.repositories.UserRepository

@ExperimentalSettingsApi
@Suppress("LongMethod")
@FlowPreview
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val baseUrl = BuildConfig.BASE_URL
        val eventId = BuildConfig.EVENT_ID
        val db = DatabaseWrapper(context = this).createDb()
        val api = ConferenceApi.create(
            baseUrl = baseUrl, eventId = eventId, enableNetworkLogs = BuildConfig.DEBUG
        )
        val settings = AndroidSettings(getSharedPreferences(eventId, MODE_PRIVATE))
        val agendaRepository = AgendaRepository.Factory.create(
            api = api,
            scheduleDao = ScheduleDao(db, settings, eventId),
            speakerDao = SpeakerDao(db, eventId),
            talkDao = TalkDao(db),
            eventDao = EventDao(db, eventId),
            qrCodeGenerator = QrCodeGeneratorAndroid()
        )
        val userRepository = UserRepository.Factory.create(
            userDao = UserDao(db = db, settings = settings, eventId = eventId),
            qrCodeGenerator = QrCodeGeneratorAndroid()
        )
        val speakerRepository = SpeakerRepository.Factory.create(
            speakerDao = SpeakerDao(db, eventId)
        )
        val openFeedbackState = (application as MainApplication).openFeedbackConfig
        setContent {
            val reportSubject = stringResource(id = R.string.text_report_subject)
            val reportAppTarget = stringResource(id = R.string.text_report_app_target)
            Main(
                agendaRepository = agendaRepository,
                userRepository = userRepository,
                speakerRepository = speakerRepository,
                alarmIntentFactory = AlarmIntentFactoryImpl,
                openFeedbackState = openFeedbackState,
                launchUrl = { launchUrl(it) },
                onReportClicked = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:${BuildConfig.CONTACT_MAIL}")
                        putExtra(Intent.EXTRA_SUBJECT, reportSubject)
                    }
                    startActivity(Intent.createChooser(intent, reportAppTarget))
                },
                onShareClicked = { text ->
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, text)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                },
                onItineraryClicked = { lat, lng ->
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("google.navigation:q=$lat,$lng")
                    }
                    val shareIntent = Intent.createChooser(intent, null)
                    startActivity(shareIntent)
                }
            )
        }
    }

    companion object {
        fun create(context: Context) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}
