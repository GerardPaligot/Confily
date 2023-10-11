package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakersUiState
import org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakersViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.screens.speakers.SpeakersList
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.SpeakerRepository

@Composable
fun SpeakersListVM(
    speakerRepository: SpeakerRepository,
    onSpeakerClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakersViewModel = viewModel(
        factory = org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakersViewModel.Factory.create(speakerRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakersUiState.Loading -> SpeakersList(
            speakers = (uiState.value as org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakersUiState.Loading).speakers,
            isLoading = true,
            modifier = modifier,
            onSpeakerClicked = onSpeakerClicked
        )
        is org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakersUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakersUiState.Success -> SpeakersList(
            speakers = (uiState.value as org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakersUiState.Success).speakers,
            isLoading = false,
            modifier = modifier,
            onSpeakerClicked = onSpeakerClicked
        )
    }
}
