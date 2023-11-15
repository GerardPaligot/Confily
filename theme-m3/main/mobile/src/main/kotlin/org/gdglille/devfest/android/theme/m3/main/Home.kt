package org.gdglille.devfest.android.theme.m3.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.AlarmScheduler
import org.gdglille.devfest.android.theme.m3.infos.feature.InfoCompactVM
import org.gdglille.devfest.android.theme.m3.navigation.ActionIds
import org.gdglille.devfest.android.theme.m3.navigation.Screen
import org.gdglille.devfest.android.theme.m3.networking.feature.NetworkingCompactVM
import org.gdglille.devfest.android.theme.m3.partners.feature.PartnersListCompactVM
import org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleListCompactVM
import org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakersListCompactVM
import org.gdglille.devfest.android.theme.m3.style.appbars.BottomAppBar
import org.gdglille.devfest.models.ui.ExportNetworkingUi
import org.gdglille.devfest.models.ui.VCardModel
import org.gdglille.devfest.models.ui.convertToModelUi
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.EventRepository
import org.gdglille.devfest.repositories.SpeakerRepository
import org.gdglille.devfest.repositories.UserRepository

@Suppress("LongMethod", "UnusedPrivateMember", "ComplexMethod")
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun Home(
    agendaRepository: AgendaRepository,
    userRepository: UserRepository,
    speakerRepository: SpeakerRepository,
    eventRepository: EventRepository,
    alarmScheduler: AlarmScheduler,
    onFilterClicked: () -> Unit,
    onTalkClicked: (id: String) -> Unit,
    onLinkClicked: (url: String?) -> Unit,
    onSpeakerClicked: (id: String) -> Unit,
    onPartnerClicked: (id: String) -> Unit,
    onContactScannerClicked: () -> Unit,
    onContactExportClicked: (ExportNetworkingUi) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onTicketScannerClicked: () -> Unit,
    onCreateProfileClicked: () -> Unit,
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    onDisconnectedClicked: () -> Unit,
    modifier: Modifier = Modifier,
    savedStateHandle: SavedStateHandle? = null,
    navController: NavHostController = rememberNavController(),
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory.create(agendaRepository, userRepository)
    )
) {
    if (savedStateHandle != null) {
        val qrCodeTicket by savedStateHandle.getLiveData<String>(HomeResultKey.QR_CODE_TICKET)
            .observeAsState()
        val qrCodeVCard by savedStateHandle.getLiveData<VCardModel>(HomeResultKey.QR_CODE_VCARD)
            .observeAsState()
        LaunchedEffect(qrCodeTicket, qrCodeVCard) {
            qrCodeTicket?.let {
                viewModel.saveTicket(it)
                savedStateHandle.remove<String>(HomeResultKey.QR_CODE_TICKET)
            }
            qrCodeVCard?.let {
                viewModel.saveNetworkingProfile(it.convertToModelUi())
                savedStateHandle.remove<String>(HomeResultKey.QR_CODE_VCARD)
            }
        }
    }
    val uiState = viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.screenConfig(Screen.Agenda.route)
    }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        destination.route?.let { route -> viewModel.screenConfig(route) }
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val route = currentDestination?.route ?: Screen.Agenda.route
    when (uiState.value) {
        is HomeUiState.Success -> {
            val screenUi = (uiState.value as HomeUiState.Success).screenUi
            Scaffold(
                modifier = modifier,
                bottomBar = {
                    if (screenUi.bottomActionsUi.actions.isNotEmpty()) {
                        BottomAppBar(
                            bottomActions = screenUi.bottomActionsUi,
                            routeSelected = route,
                            onClick = {
                                navController.navigate(it.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                },
                floatingActionButton = {
                    screenUi.fabAction?.let { fabAction ->
                        ExtendedFloatingActionButton(
                            text = { Text(text = stringResource(fabAction.label)) },
                            icon = {
                                Icon(
                                    imageVector = fabAction.icon,
                                    contentDescription = fabAction.contentDescription
                                        ?.let { stringResource(it) }
                                )
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            onClick = {
                                when (fabAction.id) {
                                    ActionIds.SCAN_TICKET -> {
                                        onTicketScannerClicked()
                                    }

                                    ActionIds.CREATE_PROFILE -> {
                                        onCreateProfileClicked()
                                    }

                                    ActionIds.SCAN_CONTACTS -> {
                                        onContactScannerClicked()
                                    }

                                    else -> TODO("Fab not implemented")
                                }
                            }
                        )
                    }
                },
                content = {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Agenda.route,
                        modifier = Modifier.padding(it),
                        builder = {
                            composable(Screen.Agenda.route) {
                                ScheduleListCompactVM(
                                    agendaRepository = agendaRepository,
                                    alarmScheduler = alarmScheduler,
                                    onFilterClicked = onFilterClicked,
                                    onTalkClicked = onTalkClicked
                                )
                            }
                            composable(Screen.SpeakerList.route) {
                                SpeakersListCompactVM(
                                    speakerRepository = speakerRepository,
                                    onSpeakerClicked = onSpeakerClicked
                                )
                            }
                            composable(Screen.MyProfile.route) {
                                NetworkingCompactVM(
                                    agendaRepository = agendaRepository,
                                    userRepository = userRepository,
                                    onCreateProfileClicked = onCreateProfileClicked,
                                    onContactExportClicked = onContactExportClicked,
                                    onInnerScreenOpened = viewModel::innerScreenConfig
                                )
                            }
                            composable(Screen.Partners.route) {
                                PartnersListCompactVM(
                                    agendaRepository = agendaRepository,
                                    onPartnerClick = onPartnerClicked
                                )
                            }
                            composable(Screen.Event.route) {
                                InfoCompactVM(
                                    agendaRepository = agendaRepository,
                                    eventRepository = eventRepository,
                                    onItineraryClicked = onItineraryClicked,
                                    onLinkClicked = onLinkClicked,
                                    onDisconnectedClicked = onDisconnectedClicked,
                                    onReportByPhoneClicked = onReportByPhoneClicked,
                                    onReportByEmailClicked = onReportByEmailClicked,
                                    onInnerScreenOpened = viewModel::innerScreenConfig
                                )
                            }
                        }
                    )
                }
            )
        }

        else -> {}
    }
}
