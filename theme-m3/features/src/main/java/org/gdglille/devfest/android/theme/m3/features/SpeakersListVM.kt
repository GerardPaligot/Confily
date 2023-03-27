package org.gdglille.devfest.android.theme.m3.features

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.viewmodels.SpeakersUiState
import org.gdglille.devfest.android.data.viewmodels.SpeakersViewModel
import org.gdglille.devfest.android.screens.speakers.SpeakersList
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.SpeakerRepository

@Composable
fun SpeakersListVM(
    speakerRepository: SpeakerRepository,
    onSpeakerClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpeakersViewModel = viewModel(
        factory = SpeakersViewModel.Factory.create(speakerRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is SpeakersUiState.Loading -> SpeakersList(
            speakers = (uiState.value as SpeakersUiState.Loading).speakers,
            isLoading = true,
            modifier = modifier,
            onSpeakerClicked = onSpeakerClicked
        )
        is SpeakersUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is SpeakersUiState.Success -> SpeakersList(
            speakers = (uiState.value as SpeakersUiState.Success).speakers,
            isLoading = false,
            modifier = modifier,
            onSpeakerClicked = onSpeakerClicked
        )
    }
}
