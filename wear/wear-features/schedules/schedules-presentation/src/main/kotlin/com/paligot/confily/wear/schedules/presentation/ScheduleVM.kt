package com.paligot.confily.wear.schedules.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paligot.confily.wear.schedules.panes.SchedulePane
import com.paligot.confily.wear.theme.loading.LoadingPane
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ScheduleVM(
    sessionId: String,
    onSpeakerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = koinViewModel(parameters = { parametersOf(sessionId) })
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState.value) {
        is ScheduleUiState.Loading -> LoadingPane()

        is ScheduleUiState.Success -> {
            SchedulePane(
                modelUi = state.modelUi,
                onSpeakerClick = onSpeakerClick,
                modifier = modifier
            )
        }
    }
}
