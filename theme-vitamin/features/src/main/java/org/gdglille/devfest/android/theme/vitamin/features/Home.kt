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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.android.data.AlarmIntentFactory
import org.gdglille.devfest.android.data.models.VCardModel
import org.gdglille.devfest.android.data.models.convertToModelUi
import org.gdglille.devfest.android.theme.vitamin.features.structure.VitaminScaffoldNavigation
import org.gdglille.devfest.android.theme.vitamin.features.viewmodels.HomeUiState
import org.gdglille.devfest.android.theme.vitamin.features.viewmodels.HomeViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.ActionIds
import org.gdglille.devfest.android.theme.vitamin.ui.Screen
import org.gdglille.devfest.android.ui.resources.HomeResultKey
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.SpeakerRepository
import org.gdglille.devfest.repositories.UserRepository

@Suppress("LongMethod", "UnusedPrivateMember")
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun Home(
    agendaRepository: AgendaRepository,
    userRepository: UserRepository,
    speakerRepository: SpeakerRepository,
    alarmIntentFactory: AlarmIntentFactory,
    modifier: Modifier = Modifier,
    savedStateHandle: SavedStateHandle? = null,
    navController: NavHostController = rememberNavController(),
    onTalkClicked: (id: String) -> Unit,
    onLinkClicked: (url: String?) -> Unit,
    onSpeakerClicked: (id: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onContactScannerClicked: () -> Unit,
    onTicketScannerClicked: () -> Unit,
    onCreateProfileClicked: () -> Unit,
    onReportClicked: () -> Unit
) {
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory.create(agendaRepository, userRepository)
    )
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
    LaunchedEffect(Unit) {
        viewModel.screenConfig("agenda")
    }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        destination.route?.let { route -> viewModel.screenConfig(route) }
    }
    when (uiState.value) {
        is HomeUiState.Success -> {
            val screenUi = (uiState.value as HomeUiState.Success).screenUi
            VitaminScaffoldNavigation(
                title = screenUi.title,
                startDestination = Screen.Agenda.route,
                modifier = modifier,
                navController = navController,
                topActions = screenUi.topActions,
                tabActions = screenUi.tabActions,
                bottomActions = screenUi.bottomActions,
                fabAction = screenUi.fabAction,
                scrollable = screenUi.scrollable,
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
                navigationIcon = null,
                builder = {
                    composable(Screen.Agenda.route) {
                        AgendaVM(
                            agendaRepository = agendaRepository,
                            alarmIntentFactory = alarmIntentFactory,
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
                        MyProfileVM(
                            userRepository = userRepository,
                            onEditInformation = onCreateProfileClicked
                        )
                    }
                    composable(Screen.Contacts.route) {
                        ContactsVM(
                            userRepository = userRepository
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
                            onLinkClicked = onLinkClicked,
                            onItineraryClicked = onItineraryClicked
                        )
                    }
                    composable(Screen.Menus.route) {
                        MenusVM(agendaRepository = agendaRepository)
                    }
                    composable(Screen.QAndA.route) {
                        QAndAListVM(
                            agendaRepository = agendaRepository,
                            onLinkClicked = onLinkClicked
                        )
                    }
                    composable(Screen.CoC.route) {
                        CoCVM(
                            agendaRepository = agendaRepository,
                            onReportClicked = onReportClicked
                        )
                    }
                }
            )
        }

        else -> {}
    }
}
