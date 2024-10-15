package com.paligot.confily.schedules.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import com.paligot.confily.resources.text_loading
import com.paligot.confily.schedules.panes.ScheduleDetailEventSessionScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDetailEventSessionVM(
    scheduleId: String,
    onItineraryClicked: (lat: Double, lng: Double) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScheduleDetailEventSessionViewModel =
        koinViewModel(parameters = { parametersOf(scheduleId) })
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        content = {
            when (val uiState = viewModel.uiState.collectAsState().value) {
                is ScheduleEventUiState.Loading -> Text(text = stringResource(Resource.string.text_loading))
                is ScheduleEventUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
                is ScheduleEventUiState.Success -> ScheduleDetailEventSessionScreen(
                    session = uiState.session,
                    modifier = modifier,
                    onItineraryClicked = onItineraryClicked,
                    onBackClicked = onBackClicked
                )
            }
        }
    )
}
