package org.gdglille.devfest.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.openfeedback.android.OpenFeedbackConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.android.data.AlarmScheduler
import org.gdglille.devfest.android.theme.m2.features.Home
import org.gdglille.devfest.android.theme.m2.features.MenusVM
import org.gdglille.devfest.android.theme.m2.features.ProfileInputVM
import org.gdglille.devfest.android.theme.m2.features.ScheduleDetailVM
import org.gdglille.devfest.android.theme.m2.features.SpeakerDetailVM
import org.gdglille.devfest.android.ui.m2.screens.TicketQrCodeScanner
import org.gdglille.devfest.android.ui.m2.screens.VCardQrCodeScanner
import org.gdglille.devfest.android.ui.m2.theme.Conferences4HallTheme
import org.gdglille.devfest.android.ui.resources.HomeResultKey
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.SpeakerRepository
import org.gdglille.devfest.repositories.UserRepository

@Suppress("LongMethod", "UnusedPrivateMember")
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun Main(
    agendaRepository: AgendaRepository,
    userRepository: UserRepository,
    speakerRepository: SpeakerRepository,
    alarmScheduler: AlarmScheduler,
    openFeedbackState: OpenFeedbackConfig,
    launchUrl: (String) -> Unit,
    onReportClicked: () -> Unit,
    onShareClicked: (text: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    navController: NavHostController
) {
    Conferences4HallTheme {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()
        val statusBarColor = MaterialTheme.colors.primary
        SideEffect {
            systemUiController.setSystemBarsColor(color = statusBarColor, darkIcons = useDarkIcons)
        }
        NavHost(navController = navController, startDestination = "home") {
            composable(route = "home") {
                Home(
                    agendaRepository = agendaRepository,
                    userRepository = userRepository,
                    alarmScheduler = alarmScheduler,
                    savedStateHandle = navController.currentBackStackEntry?.savedStateHandle,
                    onTalkClicked = {
                        navController.navigate("schedules/$it")
                    },
                    onLinkClicked = { url -> url?.let { launchUrl(it) } },
                    onContactScannerClicked = {
                        navController.navigate("scanner/vcard")
                    },
                    onTicketScannerClicked = {
                        navController.navigate("scanner/ticket")
                    },
                    onCreateProfileClicked = {
                        navController.navigate("profile")
                    },
                    onMenusClicked = {
                        navController.navigate("menus")
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
                    alarmScheduler = alarmScheduler,
                    onLinkClicked = { launchUrl(it) },
                    onBackClicked = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = "scanner/vcard") {
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
            composable(route = "scanner/ticket") {
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
            composable(route = "profile") {
                ProfileInputVM(
                    userRepository = userRepository,
                    onBackClicked = {
                        navController.popBackStack()
                    }
                )
            }
            composable(route = "menus") {
                MenusVM(
                    agendaRepository = agendaRepository,
                    onBackClicked = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
