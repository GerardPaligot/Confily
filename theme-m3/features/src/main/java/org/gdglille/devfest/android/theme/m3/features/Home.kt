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
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.UserRepository

@Suppress("LongMethod", "UnusedPrivateMember")
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun Home(
    agendaRepository: AgendaRepository,
    userRepository: UserRepository,
    alarmScheduler: AlarmScheduler,
    modifier: Modifier = Modifier,
    savedStateHandle: SavedStateHandle? = null,
    navController: NavHostController = rememberNavController(),
    onTalkClicked: (id: String) -> Unit,
    onLinkClicked: (url: String?) -> Unit,
    onContactScannerClicked: () -> Unit,
    onTicketScannerClicked: () -> Unit,
    onCreateProfileClicked: () -> Unit,
    onMenusClicked: () -> Unit,
    onReportClicked: () -> Unit
) {
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory.create(agendaRepository, userRepository)
    )
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
    val uiBottomState = viewModel.uiBottomState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.screenConfig(Screen.Agenda.route)
    }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        destination.route?.let { route -> viewModel.screenConfig(route) }
    }
    val actions = uiTopState.value
    when (uiState.value) {
        is HomeUiState.Success -> {
            val screenUi = (uiState.value as HomeUiState.Success).screenUi
            ScaffoldNavigation(
                title = screenUi.title,
                startDestination = Screen.Agenda.route,
                modifier = modifier,
                navController = navController,
                topActions = actions,
                bottomActions = uiBottomState.value,
                onTopActionClicked = {
                    when (it.id) {
                        ActionIds.FAVORITE -> {
                            viewModel.toggleFavoriteFiltering()
                        }

                        ActionIds.SCAN_NETWORKING -> {
                            onContactScannerClicked()
                        }

                        ActionIds.CREATE_PROFILE -> {
                            onCreateProfileClicked()
                        }

                        ActionIds.REPORT -> {
                            onReportClicked()
                        }
                    }
                },
                builder = {
                    composable(Screen.Agenda.route) {
                        AgendaVM(
                            agendaRepository = agendaRepository,
                            alarmScheduler = alarmScheduler,
                            onTalkClicked = onTalkClicked,
                        )
                    }
                    composable(Screen.Networking.route) {
                        ContactsVM(
                            userRepository = userRepository,
                        )
                    }
                    composable(Screen.Partners.route) {
                        PartnersVM(
                            agendaRepository = agendaRepository,
                            onPartnerClick = onLinkClicked
                        )
                    }
                    composable(Screen.Event.route) {
                        EventVM(
                            agendaRepository = agendaRepository,
                            modifier = modifier,
                            onLinkClicked = onLinkClicked,
                            onTicketScannerClicked = onTicketScannerClicked,
                            onMenusClicked = onMenusClicked
                        )
                    }
                }
            )
        }

        else -> {}
    }
}
