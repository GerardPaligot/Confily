package com.paligot.confily.speakers.presentation

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.text_error
import com.paligot.confily.speakers.panes.SpeakersGridScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SpeakersGridVM(
    onSpeakerClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    viewModel: SpeakersListViewModel = koinViewModel()
) {
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is SpeakersUiState.Loading -> SpeakersGridScreen(
            speakers = uiState.speakers,
            onSpeakerClicked = onSpeakerClicked,
            modifier = modifier,
            state = state,
            isLoading = true
        )

        is SpeakersUiState.Failure -> Text(text = stringResource(Resource.string.text_error))
        is SpeakersUiState.Success -> SpeakersGridScreen(
            speakers = uiState.speakers,
            onSpeakerClicked = onSpeakerClicked,
            modifier = modifier,
            state = state,
            isLoading = false
        )
    }
}
