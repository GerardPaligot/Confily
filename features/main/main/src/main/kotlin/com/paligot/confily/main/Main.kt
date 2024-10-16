package com.paligot.confily.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.paligot.confily.models.ui.ExportNetworkingUi
import com.paligot.confily.style.theme.ConfilyTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Suppress("LongMethod", "UnusedPrivateMember")
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun Main(
    defaultEvent: String?,
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
    ConfilyTheme {
        val uiState = viewModel.uiState.collectAsState()
        when (uiState.value) {
            is MainUiState.Success -> {
                MainNavigation(
                    startDestination = (uiState.value as MainUiState.Success).startDestination,
                    launchUrl = launchUrl,
                    onContactExportClicked = onContactExportClicked,
                    onReportByPhoneClicked = onReportByPhoneClicked,
                    onReportByEmailClicked = onReportByEmailClicked,
                    onShareClicked = onShareClicked,
                    onItineraryClicked = onItineraryClicked,
                    onScheduleStarted = onScheduleStarted,
                    onProfileCreated = onProfileCreated,
                    navController = navController
                )
            }

            else -> {}
        }
    }
}
