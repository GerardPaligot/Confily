package org.gdglille.devfest.android.theme.m3.speakers.feature

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.m3.speakers.screens.SpeakersGridScreen
import org.gdglille.devfest.android.theme.m3.style.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun SpeakersGridVM(
    onSpeakerClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpeakersListViewModel = koinViewModel()
) {
    val state = rememberLazyGridState()
    when (val uiState = viewModel.uiState.collectAsState().value) {
        is SpeakersUiState.Loading -> SpeakersGridScreen(
            speakers = uiState.speakers,
            onSpeakerClicked = onSpeakerClicked,
            modifier = modifier,
            state = state,
            isLoading = true
        )

        is SpeakersUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is SpeakersUiState.Success -> SpeakersGridScreen(
            speakers = uiState.speakers,
            onSpeakerClicked = onSpeakerClicked,
            modifier = modifier,
            state = state,
            isLoading = false
        )
    }
}
