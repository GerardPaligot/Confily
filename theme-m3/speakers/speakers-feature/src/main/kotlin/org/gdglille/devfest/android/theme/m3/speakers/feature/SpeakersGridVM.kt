package org.gdglille.devfest.android.theme.m3.speakers.feature

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import org.gdglille.devfest.android.shared.resources.Resource
import org.gdglille.devfest.android.shared.resources.text_error
import org.gdglille.devfest.android.theme.m3.speakers.screens.SpeakersGridScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalResourceApi::class)
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
