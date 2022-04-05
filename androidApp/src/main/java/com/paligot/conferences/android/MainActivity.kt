package com.paligot.conferences.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.runtime.SideEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.paligot.conferences.android.data.QrCodeGeneratorAndroid
import com.paligot.conferences.android.screens.home.Home
import com.paligot.conferences.android.screens.profile.ProfileInputVM
import com.paligot.conferences.android.screens.scanner.QrCodeScannerVm
import com.paligot.conferences.android.screens.schedule.ScheduleDetailVM
import com.paligot.conferences.android.screens.speakers.SpeakerDetailVM
import com.paligot.conferences.database.DatabaseWrapper
import com.paligot.conferences.database.EventDao
import com.paligot.conferences.database.ScheduleDao
import com.paligot.conferences.database.SpeakerDao
import com.paligot.conferences.database.TalkDao
import com.paligot.conferences.database.UserDao
import com.paligot.conferences.network.ConferenceApi
import com.paligot.conferences.repositories.AgendaRepository
import com.paligot.conferences.repositories.UserRepository
import com.paligot.conferences.ui.theme.Conferences4HallTheme
import com.russhwolf.settings.AndroidSettings


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val baseUrl = "https://cms4partners-ce427.nw.r.appspot.com"
        val eventId = "2022"
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
                settings = AndroidSettings(getSharedPreferences(eventId, Context.MODE_PRIVATE)),
                eventId = eventId
            ),
            qrCodeGenerator = QrCodeGeneratorAndroid()
        )
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
                                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("contact@gdglille.org"))
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Report from Devfest Lille App")
                                intent.type = "message/rfc822"
                                startActivity(Intent.createChooser(intent, "Choose an Email client :"))
                            }
                        )
                    }
                    composable(
                        route = "schedules/{scheduleId}",
                        arguments = listOf(navArgument("scheduleId") { type = NavType.StringType })
                    ) {
                        ScheduleDetailVM(
                            scheduleId = it.arguments?.getString("scheduleId")!!,
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
