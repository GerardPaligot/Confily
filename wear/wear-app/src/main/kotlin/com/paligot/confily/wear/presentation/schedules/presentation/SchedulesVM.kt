package com.paligot.confily.wear.presentation.schedules.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paligot.confily.wear.presentation.theme.loading.LoadingPane
import org.koin.androidx.compose.koinViewModel

@Composable
fun SchedulesVM(
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SchedulesViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState.value) {
        is SchedulesUiState.Loading -> LoadingPane()

        is SchedulesUiState.Success -> {
            HorizontalSchedulePager(
                modelUi = state.modelUi,
                modifier = modifier,
                onClick = onClick
            )
        }
    }
}
