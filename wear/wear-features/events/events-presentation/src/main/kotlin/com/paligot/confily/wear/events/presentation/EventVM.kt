package com.paligot.confily.wear.events.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.paligot.confily.wear.events.panes.EventPane
import com.paligot.confily.wear.theme.loading.LoadingPane
import org.koin.androidx.compose.koinViewModel

@Composable
fun EventVM(
    onSchedulesClick: () -> Unit,
    onSpeakersClick: () -> Unit,
    onPartnersClick: () -> Unit,
    onChangeEventClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EventViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    when (val state = uiState.value) {
        is EventUiState.Loading -> LoadingPane()

        is EventUiState.Success -> {
            EventPane(
                modelUi = state.modelUi,
                modifier = modifier,
                onSchedulesClick = onSchedulesClick,
                onSpeakersClick = onSpeakersClick,
                onPartnersClick = onPartnersClick,
                onChangeEventClick = {
                    viewModel.changeEvent()
                    onChangeEventClick()
                }
            )
        }
    }
}
