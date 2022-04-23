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
import org.gdglille.devfest.android.data.QrCodeGeneratorAndroid
import org.gdglille.devfest.android.screens.home.Home
import org.gdglille.devfest.android.screens.profile.ProfileInputVM
import org.gdglille.devfest.android.screens.scanner.QrCodeScannerVm
import org.gdglille.devfest.android.screens.schedule.ScheduleDetailVM
import org.gdglille.devfest.android.screens.speakers.SpeakerDetailVM
import org.gdglille.devfest.android.theme.Conferences4HallTheme
import com.russhwolf.settings.AndroidSettings
import io.openfeedback.android.OpenFeedbackConfig
import io.openfeedback.android.components.rememberOpenFeedbackState
import org.gdglille.devfest.database.*
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
        setContent {
            val openFeedbackState = rememberOpenFeedbackState(
                projectId = BuildConfig.OPENFEEDBACK_PROJECT_ID,
                firebaseConfig = OpenFeedbackConfig.FirebaseConfig(
                    projectId = BuildConfig.FIREBASE_PROJECT_ID,
                    applicationId =  BuildConfig.APPLICATION_ID,
                    apiKey = BuildConfig.FIREBASE_API_KEY,
                    databaseUrl = "https://${BuildConfig.FIREBASE_PROJECT_ID}.firebaseio.com"
                )
            )
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
}
