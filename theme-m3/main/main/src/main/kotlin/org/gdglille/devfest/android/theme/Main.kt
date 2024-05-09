package org.gdglille.devfest.android.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import io.openfeedback.viewmodels.OpenFeedbackFirebaseConfig
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.android.theme.m3.style.Conferences4HallTheme
import org.gdglille.devfest.models.ui.ExportNetworkingUi
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Suppress("LongMethod", "UnusedPrivateMember")
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun Main(
    defaultEvent: String?,
    openfeedbackFirebaseConfig: OpenFeedbackFirebaseConfig,
    launchUrl: (String) -> Unit,
    onContactExportClicked: (ExportNetworkingUi) -> Unit,
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onScheduleStarted: () -> Unit,
    onProfileCreated: () -> Unit,
    navController: NavHostController,
    viewModel: MainViewModel = koinViewModel(parameters = { parametersOf(defaultEvent) })
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
                    onScheduleStarted = onScheduleStarted,
                    onProfileCreated = onProfileCreated,
                    savedStateHandle = navController.currentBackStackEntry?.savedStateHandle,
                    navController = navController
                )
            }

            else -> {}
        }
    }
}
