package org.gdglille.devfest.android.theme.m3.speakers.feature

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.gdglille.devfest.android.theme.m3.speakers.screens.SpeakersListScreen
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.Scaffold
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpeakersListCompactVM(
    onSpeakerClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpeakersListViewModel = koinViewModel()
) {
    val state = rememberLazyGridState()
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(
        title = stringResource(id = R.string.screen_speakers),
        modifier = modifier,
        hasScrollBehavior = false
    ) {
        when (uiState.value) {
            is SpeakersUiState.Loading -> SpeakersListScreen(
                speakers = (uiState.value as SpeakersUiState.Loading).speakers,
                onSpeakerClicked = onSpeakerClicked,
                modifier = Modifier.padding(it),
                state = state,
                isLoading = true
            )

            is SpeakersUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
            is SpeakersUiState.Success -> {
                SpeakersListScreen(
                    speakers = (uiState.value as SpeakersUiState.Success).speakers,
                    onSpeakerClicked = onSpeakerClicked,
                    modifier = Modifier.padding(top = it.calculateTopPadding()),
                    state = state,
                    isLoading = false
                )
            }
        }
    }
}
