package org.gdglille.devfest.android.theme.m3.speakers.feature

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.theme.m3.speakers.screens.SpeakersListScreen
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.android.theme.m3.style.Scaffold
import org.gdglille.devfest.repositories.SpeakerRepository

private const val ColumnCountLandscape = 4
private const val ColumnCountPortrait = 2

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpeakersListCompactVM(
    speakerRepository: SpeakerRepository,
    onSpeakerClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpeakersListViewModel = viewModel(
        factory = SpeakersListViewModel.Factory.create(speakerRepository)
    )
) {
    val configuration = LocalConfiguration.current
    val state = rememberLazyGridState()
    val uiState = viewModel.uiState.collectAsState()
    val columnCount =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) ColumnCountLandscape
        else ColumnCountPortrait
    Scaffold(
        title = stringResource(id = R.string.screen_speakers),
        modifier = modifier
    ) {
        when (uiState.value) {
            is SpeakersUiState.Loading -> SpeakersListScreen(
                speakers = (uiState.value as SpeakersUiState.Loading).speakers,
                columnCount = columnCount,
                state = state,
                onSpeakerClicked = onSpeakerClicked,
                isLoading = true,
                modifier = Modifier.padding(it)
            )

            is SpeakersUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
            is SpeakersUiState.Success -> {
                SpeakersListScreen(
                    speakers = (uiState.value as SpeakersUiState.Success).speakers,
                    columnCount = columnCount,
                    state = state,
                    onSpeakerClicked = onSpeakerClicked,
                    isLoading = false,
                    modifier = Modifier.padding(it)
                )
            }
        }
    }
}
