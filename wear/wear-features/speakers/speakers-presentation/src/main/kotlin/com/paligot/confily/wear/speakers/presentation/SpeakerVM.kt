package com.paligot.confily.wear.speakers.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paligot.confily.wear.speakers.panes.SpeakerPane
import com.paligot.confily.wear.theme.loading.LoadingPane
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SpeakerVM(
    speakerId: String,
    onSessionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpeakerViewModel = koinViewModel(parameters = { parametersOf(speakerId) })
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState.value) {
        is SpeakerUiState.Loading -> LoadingPane()

        is SpeakerUiState.Success -> {
            SpeakerPane(
                modelUi = state.modelUi,
                modifier = modifier,
                onSessionClick = onSessionClick
            )
        }
    }
}
