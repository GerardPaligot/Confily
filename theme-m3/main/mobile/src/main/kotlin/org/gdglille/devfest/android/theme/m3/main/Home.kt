package org.gdglille.devfest.android.theme.m3.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.AlarmScheduler
import org.gdglille.devfest.models.ui.VCardModel
import org.gdglille.devfest.models.ui.convertToModelUi
import org.gdglille.devfest.android.theme.m3.infos.feature.InfoPages
import org.gdglille.devfest.android.theme.m3.navigation.ActionIds
import org.gdglille.devfest.android.theme.m3.navigation.Screen
import org.gdglille.devfest.android.theme.m3.networking.feature.NetworkingPages
import org.gdglille.devfest.android.theme.m3.partners.feature.PartnersListVM
import org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleListVM
import org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakersListVM
import org.gdglille.devfest.models.ui.ExportNetworkingUi
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.EventRepository
import org.gdglille.devfest.repositories.SpeakerRepository
import org.gdglille.devfest.repositories.UserRepository

@OptIn(ExperimentalFoundationApi::class)
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
        factory = HomeViewModel.Factory.create(agendaRepository, userRepository, eventRepository)
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
    val exportPath = viewModel.exportPath.collectAsState(null)
    val currentRoute = navController
        .currentBackStackEntryFlow
        .collectAsState(initial = navController.currentBackStackEntry)
    LaunchedEffect(Unit) {
        viewModel.screenConfig(Screen.Agenda.route)
    }
    LaunchedEffect(exportPath.value) {
        exportPath.value?.let(onContactExportClicked)
    }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        destination.route?.let { route -> viewModel.screenConfig(route) }
    }
    when (uiState.value) {
        is HomeUiState.Success -> {
            val screenUi = (uiState.value as HomeUiState.Success).screenUi
            val pageCount = if (screenUi.tabActionsUi.actions.isEmpty()) 1
            else screenUi.tabActionsUi.actions.count()
            val agendaPagerState = rememberPagerState(pageCount = { pageCount })
            val networkingPagerState = rememberPagerState(pageCount = { pageCount })
            val infoPagerState = rememberPagerState(pageCount = { pageCount })
            ScaffoldNavigation(
                title = screenUi.title,
                startDestination = Screen.Agenda.route,
                modifier = modifier,
                navController = navController,
                topActions = screenUi.topActionsUi,
                tabActions = screenUi.tabActionsUi,
                bottomActions = screenUi.bottomActionsUi,
                fabAction = screenUi.fabAction,
                pagerState = when (currentRoute.value?.destination?.route) {
                    Screen.Event.route, Screen.Menus.route, Screen.QAndA.route, Screen.CoC.route -> infoPagerState
                    Screen.MyProfile.route, Screen.Contacts.route -> networkingPagerState
                    else -> agendaPagerState
                },
                onTopActionClicked = {
                    when (it.id) {
                        ActionIds.FILTERS -> {
                            onFilterClicked()
                        }

                        ActionIds.DISCONNECT -> {
                            viewModel.disconnect()
                            onDisconnectedClicked()
                        }

                        ActionIds.EXPORT -> {
                            viewModel.exportNetworking()
                        }
                    }
                },
                onFabActionClicked = {
                    when (it.id) {
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
                },
                builder = {
                    composable(Screen.Agenda.route) {
                        ScheduleListVM(
                            tabs = screenUi.tabActionsUi,
                            agendaRepository = agendaRepository,
                            alarmScheduler = alarmScheduler,
                            pagerState = agendaPagerState,
                            onTalkClicked = onTalkClicked,
                        )
                    }
                    composable(Screen.SpeakerList.route) {
                        SpeakersListVM(
                            speakerRepository = speakerRepository,
                            onSpeakerClicked = onSpeakerClicked
                        )
                    }
                    composable(Screen.MyProfile.route) {
                        NetworkingPages(
                            tabs = screenUi.tabActionsUi,
                            userRepository = userRepository,
                            pagerState = networkingPagerState,
                            onCreateProfileClicked = onCreateProfileClicked,
                            onInnerScreenOpened = viewModel::innerScreenConfig
                        )
                    }
                    composable(Screen.Partners.route) {
                        PartnersListVM(
                            agendaRepository = agendaRepository,
                            onPartnerClick = onPartnerClicked
                        )
                    }
                    composable(Screen.Event.route) {
                        InfoPages(
                            tabs = screenUi.tabActionsUi,
                            agendaRepository = agendaRepository,
                            pagerState = infoPagerState,
                            onItineraryClicked = onItineraryClicked,
                            onLinkClicked = onLinkClicked,
                            onReportByPhoneClicked = onReportByPhoneClicked,
                            onReportByEmailClicked = onReportByEmailClicked,
                            onInnerScreenOpened = viewModel::innerScreenConfig
                        )
                    }
                }
            )
        }

        else -> {}
    }
}
