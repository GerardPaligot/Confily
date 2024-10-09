package com.paligot.confily.wear.speakers.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paligot.confily.wear.speakers.panes.SpeakersPane
import com.paligot.confily.wear.theme.loading.LoadingPane
import org.koin.androidx.compose.koinViewModel

@Composable
fun SpeakersVM(
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpeakersViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState.value) {
        is SpeakersUiState.Loading -> LoadingPane()

        is SpeakersUiState.Success -> {
            SpeakersPane(
                modelUi = state.modelUi,
                modifier = modifier,
                onClick = onClick
            )
        }
    }
}
