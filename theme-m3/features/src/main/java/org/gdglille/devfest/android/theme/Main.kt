package org.gdglille.devfest.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.openfeedback.android.OpenFeedbackConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.android.data.AlarmScheduler
import org.gdglille.devfest.android.data.viewmodels.MainUiState
import org.gdglille.devfest.android.data.viewmodels.MainViewModel
import org.gdglille.devfest.android.screens.event.TicketQrCodeScanner
import org.gdglille.devfest.android.screens.networking.VCardQrCodeScanner
import org.gdglille.devfest.android.theme.m3.features.EventListVM
import org.gdglille.devfest.android.theme.m3.features.Home
import org.gdglille.devfest.android.theme.m3.features.PartnerDetailVM
import org.gdglille.devfest.android.theme.m3.features.ProfileInputVM
import org.gdglille.devfest.android.theme.m3.features.ScheduleDetailVM
import org.gdglille.devfest.android.theme.m3.features.SpeakerDetailVM
import org.gdglille.devfest.android.ui.resources.HomeResultKey
import org.gdglille.devfest.models.ExportNetworkingUi
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.EventRepository
import org.gdglille.devfest.repositories.SpeakerRepository
import org.gdglille.devfest.repositories.UserRepository

@Suppress("LongMethod", "UnusedPrivateMember")
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun Main(
    eventRepository: EventRepository,
    agendaRepository: AgendaRepository,
    userRepository: UserRepository,
    speakerRepository: SpeakerRepository,
    alarmScheduler: AlarmScheduler,
    openFeedbackState: OpenFeedbackConfig,
    launchUrl: (String) -> Unit,
    onContactExportClicked: (ExportNetworkingUi) -> Unit,
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    navController: NavHostController,
    viewModel: MainViewModel = viewModel(
        factory = MainViewModel.Factory.create(eventRepository)
    )
) {
    val rootUri = "c4h://event"
    Conferences4HallTheme {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()
        val statusBarColor = MaterialTheme.colorScheme.surface
        SideEffect {
            systemUiController.setSystemBarsColor(color = statusBarColor, darkIcons = useDarkIcons)
        }
        val uiState = viewModel.uiState.collectAsState()
        when (uiState.value) {
            is MainUiState.Success -> {
                val startDestination = if ((uiState.value as MainUiState.Success).initialized) {
                    "home"
                } else {
                    "events"
                }
                NavHost(navController = navController, startDestination = startDestination) {
                    composable(route = "events") {
                        EventListVM(
                            repository = eventRepository,
                            onEventClicked = {
                                navController.navigate("home") {
                                    this.popUpTo("events") {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                    composable(
                        route = "home",
                        deepLinks = listOf(navDeepLink { uriPattern = rootUri })
                    ) {
                        Home(
                            agendaRepository = agendaRepository,
                            userRepository = userRepository,
                            speakerRepository = speakerRepository,
                            eventRepository = eventRepository,
                            alarmScheduler = alarmScheduler,
                            savedStateHandle = navController.currentBackStackEntry?.savedStateHandle,
                            onTalkClicked = {
                                navController.navigate("schedules/$it")
                            },
                            onLinkClicked = { url -> url?.let { launchUrl(it) } },
                            onSpeakerClicked = {
                                navController.navigate("speakers/$it")
                            },
                            onPartnerClicked = {
                                navController.navigate("partners/$it")
                            },
                            onContactScannerClicked = {
                                navController.navigate("scanner/vcard")
                            },
                            onContactExportClicked = onContactExportClicked,
                            onItineraryClicked = onItineraryClicked,
                            onTicketScannerClicked = {
                                navController.navigate("scanner/ticket")
                            },
                            onCreateProfileClicked = {
                                navController.navigate("profile")
                            },
                            onReportByPhoneClicked = onReportByPhoneClicked,
                            onReportByEmailClicked = onReportByEmailClicked,
                            onDisconnectedClicked = {
                                navController.navigate("events") {
                                    popUpTo("home") {
                                        inclusive = true
                                    }
                                }
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
                            onShareClicked = onShareClicked
                        )
                    }
                    composable(
                        route = "partners/{partnerId}",
                        arguments = listOf(navArgument("partnerId") { type = NavType.StringType }),
                        deepLinks = listOf(navDeepLink { uriPattern = "$rootUri/partners/{partnerId}" })
                    ) {
                        PartnerDetailVM(
                            partnerId = it.arguments?.getString("partnerId")!!,
                            agendaRepository = agendaRepository,
                            onLinkClicked = { launchUrl(it) },
                            onItineraryClicked = onItineraryClicked,
                            onBackClicked = {
                                navController.popBackStack()
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
                            alarmScheduler = alarmScheduler,
                            onTalkClicked = {
                                navController.navigate("schedules/$it")
                            },
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
                            },
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable(route = "profile") {
                        ProfileInputVM(
                            userRepository = userRepository,
                            onBackClicked = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }

            else -> {}
        }
    }
}
