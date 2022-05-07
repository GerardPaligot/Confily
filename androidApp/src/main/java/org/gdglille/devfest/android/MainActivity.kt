package org.gdglille.devfest.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.russhwolf.settings.AndroidSettings
import org.gdglille.devfest.android.data.QrCodeGeneratorAndroid
import org.gdglille.devfest.android.screens.home.Home
import org.gdglille.devfest.android.screens.profile.ProfileInputVM
import org.gdglille.devfest.android.screens.scanner.QrCodeScannerVm
import org.gdglille.devfest.android.screens.schedule.ScheduleDetailVM
import org.gdglille.devfest.android.screens.speakers.SpeakerDetailVM
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import org.gdglille.devfest.database.DatabaseWrapper
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.database.ScheduleDao
import org.gdglille.devfest.database.SpeakerDao
import org.gdglille.devfest.database.TalkDao
import org.gdglille.devfest.database.UserDao
import org.gdglille.devfest.network.ConferenceApi
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.UserRepository


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val baseUrl = BuildConfig.BASE_URL
        val eventId = BuildConfig.EVENT_ID
        val db = DatabaseWrapper(context = this).createDb()
        val api = ConferenceApi.create(
            baseUrl = baseUrl, eventId = eventId, enableNetworkLogs = BuildConfig.DEBUG
        )
        val agendaRepository = AgendaRepository.Factory.create(
            api = api,
            scheduleDao = ScheduleDao(db, eventId),
            speakerDao = SpeakerDao(db),
            talkDao = TalkDao(db),
            eventDao = EventDao(db, eventId)
        )
        val userRepository = UserRepository.Factory.create(
            userDao = UserDao(
                db = db,
                settings = AndroidSettings(getSharedPreferences(eventId, MODE_PRIVATE)),
                eventId = eventId
            ),
            qrCodeGenerator = QrCodeGeneratorAndroid()
        )
        val openFeedbackState = (application as MainApplication).openFeedbackConfig
        setContent {
            Conferences4HallTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = MaterialTheme.colors.isLight
                val navBarColor = MaterialTheme.colors.primarySurface
                SideEffect {
                    systemUiController.setSystemBarsColor(color = navBarColor, darkIcons = useDarkIcons)
                    systemUiController.setNavigationBarColor(color = navBarColor, darkIcons = useDarkIcons)
                }
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable(route = "home") {
                        val reportSubject = stringResource(id = R.string.text_report_subject)
                        val reportAppTarget = stringResource(id = R.string.text_report_app_target)
                        Home(
                            agendaRepository = agendaRepository,
                            userRepository = userRepository,
                            onTalkClicked = {
                                navController.navigate("schedules/$it")
                            },
                            onFaqClick = { launchUrl(it) },
                            onCoCClick = { launchUrl(it) },
                            onTwitterClick = {
                                it?.let { launchUrl(it) }
                            },
                            onLinkedInClick = {
                                it?.let { launchUrl(it) }
                            },
                            onPartnerClick = {
                                it?.let { launchUrl(it) }
                            },
                            onScannerClicked = {
                                navController.navigate("scanner")
                            },
                            onQrCodeClicked = {
                                navController.navigate("profile")
                            },
                            onReportClicked = {
                                val intent = Intent(Intent.ACTION_SEND)
                                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(BuildConfig.CONTACT_MAIL))
                                intent.putExtra(Intent.EXTRA_SUBJECT, reportSubject)
                                intent.type = "message/rfc822"
                                startActivity(Intent.createChooser(intent, reportAppTarget))
                            }
                        )
                    }
                    composable(
                        route = "schedules/{scheduleId}",
                        arguments = listOf(navArgument("scheduleId") { type = NavType.StringType })
                    ) {
                        ScheduleDetailVM(
                            scheduleId = it.arguments?.getString("scheduleId")!!,
                            openFeedbackState = openFeedbackState,
                            agendaRepository = agendaRepository,
                            onBackClicked = {
                                navController.popBackStack()
                            },
                            onSpeakerClicked = {
                                navController.navigate("speakers/$it")
                            },
                            onShareClicked = { text ->
                                val sendIntent: Intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, text)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, null)
                                startActivity(shareIntent)
                            }
                        )
                    }
                    composable(
                        route = "speakers/{speakerId}",
                        arguments = listOf(navArgument("speakerId") { type = NavType.StringType })
                    ) {
                        SpeakerDetailVM(
                            speakerId = it.arguments?.getString("speakerId")!!,
                            agendaRepository = agendaRepository,
                            onTwitterClick = { launchUrl(it) },
                            onGitHubClick = { launchUrl(it) },
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(
                        route = "scanner"
                    ) {
                        QrCodeScannerVm(
                            userRepository = userRepository,
                            navigateToSettingsScreen = {},
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(
                        route = "profile"
                    ) {
                        ProfileInputVM(
                            userRepository = userRepository,
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val ID = "schedule.id"

        fun create(context: Context, id: String) = Intent(context, MainActivity::class.java).apply {
            putExtra(ID, id)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }
}
