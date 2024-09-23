package com.paligot.confily.wear.presentation.events.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paligot.confily.wear.presentation.theme.loading.LoadingPane
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListEventVM(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListEventViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState.value) {
        is ListEventUiState.Loading -> LoadingPane()

        is ListEventUiState.Success -> {
            ListEventPane(
                modelUi = state.modelUi,
                modifier = modifier,
                onClick = {
                    viewModel.onEventClick(it)
                    onClick()
                }
            )
        }
    }
}
