package org.gdglille.devfest.android.theme.vitamin.features

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
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.android.data.AlarmScheduler
import org.gdglille.devfest.android.data.models.VCardModel
import org.gdglille.devfest.android.data.models.convertToModelUi
import org.gdglille.devfest.android.theme.vitamin.features.structure.VitaminScaffoldNavigation
import org.gdglille.devfest.android.theme.vitamin.features.viewmodels.HomeUiState
import org.gdglille.devfest.android.theme.vitamin.features.viewmodels.HomeViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.ActionIds
import org.gdglille.devfest.android.theme.vitamin.ui.Screen
import org.gdglille.devfest.android.ui.resources.HomeResultKey
import org.gdglille.devfest.models.ExportNetworkingUi
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.EventRepository
import org.gdglille.devfest.repositories.SpeakerRepository
import org.gdglille.devfest.repositories.UserRepository

@OptIn(ExperimentalPagerApi::class)
@Suppress("LongMethod", "UnusedPrivateMember")
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun Home(
    agendaRepository: AgendaRepository,
    userRepository: UserRepository,
    speakerRepository: SpeakerRepository,
    eventRepository: EventRepository,
    alarmScheduler: AlarmScheduler,
    onTalkClicked: (id: String) -> Unit,
    onLinkClicked: (url: String?) -> Unit,
    onSpeakerClicked: (id: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onContactScannerClicked: () -> Unit,
    onContactExportClicked: (ExportNetworkingUi) -> Unit,
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
        val qrCodeTicket by savedStateHandle.getLiveData<String>(HomeResultKey.QR_CODE_TICKET).observeAsState()
        val qrCodeVCard by savedStateHandle.getLiveData<VCardModel>(HomeResultKey.QR_CODE_VCARD).observeAsState()
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
    val uiTopState = viewModel.uiTopState.collectAsState()
    val uiTabState = viewModel.uiTabState.collectAsState()
    val uiFabState = viewModel.uiFabState.collectAsState()
    val uiBottomState = viewModel.uiBottomState.collectAsState()
    val exportPath = viewModel.exportPath.collectAsState(null)
    val pagerState = rememberPagerState()
    LaunchedEffect(Unit) {
        viewModel.screenConfig(Screen.Agenda.route)
    }
    LaunchedEffect(exportPath.value) {
        exportPath.value?.let(onContactExportClicked)
    }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        destination.route?.let { route -> viewModel.screenConfig(route) }
    }
    val tabs = uiTabState.value
    val actions = uiTopState.value
    when (uiState.value) {
        is HomeUiState.Success -> {
            val screenUi = (uiState.value as HomeUiState.Success).screenUi
            VitaminScaffoldNavigation(
                title = screenUi.title,
                startDestination = Screen.Agenda.route,
                modifier = modifier,
                navController = navController,
                pagerState = pagerState,
                topActionsUi = actions,
                tabActionsUi = tabs,
                bottomActionsUi = uiBottomState.value,
                fabAction = uiFabState.value,
                onTopActionClicked = {
                    when (it.id) {
                        ActionIds.FAVORITE -> {
                            viewModel.toggleFavoriteFiltering()
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

                        ActionIds.SCAN_CONTACTS -> {
                            onContactScannerClicked()
                        }

                        ActionIds.CREATE_PROFILE -> {
                            onCreateProfileClicked()
                        }

                        else -> TODO("Fab not implemented")
                    }
                },
                navigationIcon = null
            ) {
                composable(Screen.Agenda.route) {
                    AgendaVM(
                        tabs = tabs,
                        agendaRepository = agendaRepository,
                        alarmScheduler = alarmScheduler,
                        pagerState = pagerState,
                        onTalkClicked = onTalkClicked,
                    )
                }
                composable(Screen.SpeakerList.route) {
                    SpeakersListVM(
                        speakerRepository = speakerRepository,
                        onSpeakerClicked = onSpeakerClicked
                    )
                }
                composable(Screen.Networking.route) {
                    NetworkingPages(
                        tabs = tabs,
                        userRepository = userRepository,
                        viewModel = viewModel,
                        pagerState = pagerState,
                        onCreateProfileClicked = onCreateProfileClicked
                    )
                }
                composable(Screen.Partners.route) {
                    PartnersVM(
                        agendaRepository = agendaRepository,
                        onPartnerClick = onLinkClicked
                    )
                }
                composable(Screen.Info.route) {
                    InfoPages(
                        tabs = tabs,
                        agendaRepository = agendaRepository,
                        viewModel = viewModel,
                        pagerState = pagerState,
                        onItineraryClicked = onItineraryClicked,
                        onLinkClicked = onLinkClicked,
                        onReportByPhoneClicked = onReportByPhoneClicked,
                        onReportByEmailClicked = onReportByEmailClicked
                    )
                }
            }
        }

        else -> {}
    }
}
