package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.decathlon.vitamin.compose.foundation.VitaminTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.openfeedback.android.OpenFeedbackConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.gdglille.devfest.android.data.AlarmIntentFactory
import org.gdglille.devfest.android.screens.scanner.vcard.VCardQrCodeScanner
import org.gdglille.devfest.android.theme.vitamin.ui.screens.TicketQrCodeScanner
import org.gdglille.devfest.android.theme.vitamin.ui.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.HomeResultKey
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.SpeakerRepository
import org.gdglille.devfest.repositories.UserRepository

@Suppress("LongMethod")
@ExperimentalCoroutinesApi
@Composable
fun Main(
    agendaRepository: AgendaRepository,
    userRepository: UserRepository,
    speakerRepository: SpeakerRepository,
    alarmIntentFactory: AlarmIntentFactory,
    openFeedbackState: OpenFeedbackConfig,
    launchUrl: (String) -> Unit,
    onReportClicked: () -> Unit,
    onShareClicked: (text: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit
) {
    Conferences4HallTheme {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()
        val statusBarColor = VitaminTheme.colors.vtmnBackgroundPrimary
        SideEffect {
            systemUiController.setSystemBarsColor(color = statusBarColor, darkIcons = useDarkIcons)
        }
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "home") {
            composable(route = "home") {
                Home(
                    agendaRepository = agendaRepository,
                    userRepository = userRepository,
                    speakerRepository = speakerRepository,
                    alarmIntentFactory = alarmIntentFactory,
                    savedStateHandle = navController.currentBackStackEntry?.savedStateHandle,
                    onTalkClicked = {
                        navController.navigate("schedules/$it")
                    },
                    onLinkClicked = { url -> url?.let { launchUrl(it) } },
                    onItineraryClicked = onItineraryClicked,
                    onSpeakerClicked = {
                        navController.navigate("speakers/$it")
                    },
                    onScannerClicked = {
                        navController.navigate("scanner/vcard")
                    },
                    onTicketScannerClicked = {
                        navController.navigate("scanner/ticket")
                    },
                    onQrCodeClicked = {
                        navController.navigate("profile")
                    },
                    onReportClicked = onReportClicked
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
                    onShareClicked = onShareClicked
                )
            }
            composable(
                route = "speakers/{speakerId}",
                arguments = listOf(navArgument("speakerId") { type = NavType.StringType })
            ) {
                SpeakerDetailVM(
                    speakerId = it.arguments?.getString("speakerId")!!,
                    agendaRepository = agendaRepository,
                    onLinkClicked = { launchUrl(it) }
                ) {
                    navController.popBackStack()
                }
            }
            composable(
                route = "scanner/vcard"
            ) {
                VCardQrCodeScanner(
                    navigateToSettingsScreen = {},
                    onQrCodeDetected = { vcard ->
                        navController
                            .previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(HomeResultKey.QR_CODE_VCARD, vcard)
                        navController.popBackStack()
                    },
                    onBackClicked = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                route = "scanner/ticket"
            ) {
                TicketQrCodeScanner(
                    navigateToSettingsScreen = {},
                    onQrCodeDetected = { barcode ->
                        navController
                            .previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(HomeResultKey.QR_CODE_TICKET, barcode)
                        navController.popBackStack()
                    }
                ) {
                    navController.popBackStack()
                }
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
