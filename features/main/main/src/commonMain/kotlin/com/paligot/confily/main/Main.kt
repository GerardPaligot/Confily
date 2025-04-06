package com.paligot.confily.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.paligot.confily.networking.ui.models.ExportNetworkingUi
import com.paligot.confily.style.components.adaptive.adaptiveInfo
import com.paligot.confily.style.theme.ConfilyTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Suppress("LongMethod", "UnusedPrivateMember")
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun Main(
    defaultEvent: String?,
    isPortrait: Boolean,
    launchUrl: (String) -> Unit,
    onContactExportClicked: (ExportNetworkingUi) -> Unit,
    onReportByPhoneClicked: (String) -> Unit,
    onReportByEmailClicked: (String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onProfileCreated: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = koinViewModel(parameters = { parametersOf(defaultEvent) })
) {
    val adaptiveInfo = adaptiveInfo()
    ConfilyTheme {
        val uiState = viewModel.uiState.collectAsState()
        when (uiState.value) {
            is MainUiState.Success -> {
                MainNavigation(
                    startDestination = (uiState.value as MainUiState.Success).startDestination,
                    isPortrait = isPortrait,
                    adaptiveInfo = adaptiveInfo,
                    launchUrl = launchUrl,
                    onContactExportClicked = onContactExportClicked,
                    onReportByPhoneClicked = onReportByPhoneClicked,
                    onReportByEmailClicked = onReportByEmailClicked,
                    onShareClicked = onShareClicked,
                    onItineraryClicked = onItineraryClicked,
                    onProfileCreated = onProfileCreated,
                    modifier = modifier,
                    navController = navController
                )
            }

            else -> {}
        }
    }
}
