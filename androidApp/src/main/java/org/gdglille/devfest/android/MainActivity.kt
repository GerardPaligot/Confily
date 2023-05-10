package org.gdglille.devfest.android

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.res.stringResource
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.AndroidContext
import org.gdglille.devfest.Platform
import org.gdglille.devfest.android.data.AlarmScheduler
import org.gdglille.devfest.android.data.QrCodeGeneratorAndroid
import org.gdglille.devfest.android.theme.Main
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.database.DatabaseWrapper
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.database.FeaturesActivatedDao
import org.gdglille.devfest.database.PartnerDao
import org.gdglille.devfest.database.ScheduleDao
import org.gdglille.devfest.database.SpeakerDao
import org.gdglille.devfest.database.TalkDao
import org.gdglille.devfest.database.UserDao
import org.gdglille.devfest.network.ConferenceApi
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.EventRepository
import org.gdglille.devfest.repositories.SpeakerRepository
import org.gdglille.devfest.repositories.UserRepository
import java.io.File

@Suppress("LongMethod")
@FlowPreview
@ExperimentalSettingsApi
@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavHostController

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val baseUrl = BuildConfig.BASE_URL
        val db = DatabaseWrapper(context = this).createDb()
        val platform = Platform(AndroidContext(this.applicationContext))
        val api = ConferenceApi.create(
            platform = platform,
            baseUrl = baseUrl,
            enableNetworkLogs = BuildConfig.DEBUG
        )
        val settings = AndroidSettings(
            getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE)
        )
        val eventRepository = EventRepository.Factory.create(
            api = api,
            eventDao = EventDao(db, settings)
        )
        val agendaRepository = AgendaRepository.Factory.create(
            api = api,
            scheduleDao = ScheduleDao(db, settings),
            speakerDao = SpeakerDao(db),
            talkDao = TalkDao(db),
            eventDao = EventDao(db, settings),
            partnerDao = PartnerDao(db = db, platform = platform),
            featuresDao = FeaturesActivatedDao(db),
            qrCodeGenerator = QrCodeGeneratorAndroid()
        )
        val userRepository = UserRepository.Factory.create(
            userDao = UserDao(db, platform),
            eventDao = EventDao(db, settings),
            qrCodeGenerator = QrCodeGeneratorAndroid()
        )
        val speakerRepository = SpeakerRepository.Factory.create(
            speakerDao = SpeakerDao(db),
            eventDao = EventDao(db, settings)
        )
        val scheduler = AlarmScheduler(
            agendaRepository,
            getSystemService(Context.ALARM_SERVICE) as AlarmManager,
            AlarmIntentFactoryImpl
        )
        val openFeedbackState = (application as MainApplication).openFeedbackConfig
        setContent {
            navController = rememberNavController()
            val exportSubject = stringResource(id = R.string.text_export_subject)
            val reportSubject = stringResource(id = R.string.text_report_subject)
            val reportAppTarget = stringResource(id = R.string.text_report_app_target)
            Main(
                eventRepository = eventRepository,
                agendaRepository = agendaRepository,
                userRepository = userRepository,
                speakerRepository = speakerRepository,
                alarmScheduler = scheduler,
                openFeedbackState = openFeedbackState,
                launchUrl = { launchUrl(it) },
                onContactExportClicked = { export ->
                    val uri: Uri = FileProvider.getUriForFile(
                        applicationContext,
                        "${applicationContext.packageName}.provider",
                        File(export.filePath)
                    )
                    val intent = ShareCompat.IntentBuilder(this)
                        .setStream(uri)
                        .intent
                        .setAction(Intent.ACTION_SENDTO)
                        .setData(Uri.parse("mailto:${export.mailto ?: ""}"))
                        .putExtra(Intent.EXTRA_SUBJECT, exportSubject)
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(intent)
                },
                onReportByPhoneClicked = {
                    val intent = Intent(Intent.ACTION_DIAL).apply {
                        data = Uri.parse("tel:$it")
                    }
                    startActivity(intent)
                },
                onReportByEmailClicked = {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:$it")
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
                },
                navController = navController
            )
        }
    }

    companion object {
        fun create(context: Context) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}
