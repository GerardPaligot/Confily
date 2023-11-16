package org.gdglille.devfest.android.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.openfeedback.android.viewmodels.OpenFeedbackFirebaseConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.AlarmScheduler
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.ExportNetworkingUi
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
    openfeedbackFirebaseConfig: OpenFeedbackFirebaseConfig,
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
    Conferences4HallTheme {
        val uiState = viewModel.uiState.collectAsState()
        when (uiState.value) {
            is MainUiState.Success -> {
                MainNavigation(
                    startDestination = (uiState.value as MainUiState.Success).startDestination,
                    openfeedbackFirebaseConfig = openfeedbackFirebaseConfig,
                    launchUrl = launchUrl,
                    onContactExportClicked = onContactExportClicked,
                    onReportByPhoneClicked = onReportByPhoneClicked,
                    onReportByEmailClicked = onReportByEmailClicked,
                    onShareClicked = onShareClicked,
                    onItineraryClicked = onItineraryClicked,
                    agendaRepository = agendaRepository,
                    userRepository = userRepository,
                    speakerRepository = speakerRepository,
                    eventRepository = eventRepository,
                    alarmScheduler = alarmScheduler,
                    navController = navController,
                    savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
                )
            }

            else -> {}
        }
    }
}
