package org.gdglille.devfest.android.theme.m3.features

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
import org.gdglille.devfest.android.ActionIds
import org.gdglille.devfest.android.Screen
import org.gdglille.devfest.android.data.AlarmScheduler
import org.gdglille.devfest.android.data.models.VCardModel
import org.gdglille.devfest.android.data.models.convertToModelUi
import org.gdglille.devfest.android.theme.m3.features.structure.ScaffoldNavigation
import org.gdglille.devfest.android.theme.m3.features.viewmodels.HomeUiState
import org.gdglille.devfest.android.theme.m3.features.viewmodels.HomeViewModel
import org.gdglille.devfest.android.ui.resources.HomeResultKey
import org.gdglille.devfest.models.ExportNetworkingUi
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.EventRepository
import org.gdglille.devfest.repositories.SpeakerRepository
import org.gdglille.devfest.repositories.UserRepository

@OptIn(ExperimentalPagerApi::class)
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
    val uiTopState = viewModel.uiTopState.collectAsState()
    val uiTabState = viewModel.uiTabState.collectAsState()
    val uiFabState = viewModel.uiFabState.collectAsState()
    val uiBottomState = viewModel.uiBottomState.collectAsState()
    val exportPath = viewModel.exportPath.collectAsState(null)
    val agendaPagerState = rememberPagerState()
    val networkingPagerState = rememberPagerState()
    val infoPagerState = rememberPagerState()
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
    val actions = uiTopState.value
    val tabActions = uiTabState.value
    when (uiState.value) {
        is HomeUiState.Success -> {
            val screenUi = (uiState.value as HomeUiState.Success).screenUi
            ScaffoldNavigation(
                title = screenUi.title,
                startDestination = Screen.Agenda.route,
                modifier = modifier,
                navController = navController,
                topActions = actions,
                tabActions = tabActions,
                bottomActions = uiBottomState.value,
                fabAction = uiFabState.value,
                pagerState = when (currentRoute.value?.destination?.route) {
                    Screen.Info.route -> infoPagerState
                    Screen.Networking.route -> networkingPagerState
                    else -> agendaPagerState
                },
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
                        AgendaVM(
                            tabs = tabActions,
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
                    composable(Screen.Networking.route) {
                        NetworkingPages(
                            tabs = tabActions,
                            userRepository = userRepository,
                            viewModel = viewModel,
                            pagerState = networkingPagerState,
                            onCreateProfileClicked = onCreateProfileClicked
                        )
                    }
                    composable(Screen.Partners.route) {
                        PartnersVM(
                            agendaRepository = agendaRepository,
                            onPartnerClick = onPartnerClicked
                        )
                    }
                    composable(Screen.Info.route) {
                        InfoPages(
                            tabs = tabActions,
                            agendaRepository = agendaRepository,
                            viewModel = viewModel,
                            pagerState = infoPagerState,
                            onItineraryClicked = onItineraryClicked,
                            onLinkClicked = onLinkClicked,
                            onReportByPhoneClicked = onReportByPhoneClicked,
                            onReportByEmailClicked = onReportByEmailClicked
                        )
                    }
                }
            )
        }

        else -> {}
    }
}
