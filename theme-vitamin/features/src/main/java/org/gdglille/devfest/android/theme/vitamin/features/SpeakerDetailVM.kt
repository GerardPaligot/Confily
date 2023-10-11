package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.AlarmScheduler
import org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakerUiState
import org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakerViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.screens.speakers.SpeakerDetail
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.AgendaRepository

@Composable
fun SpeakerDetailVM(
    speakerId: String,
    agendaRepository: AgendaRepository,
    alarmScheduler: AlarmScheduler,
    onTalkClicked: (id: String) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakerViewModel = viewModel(
        factory = org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakerViewModel.Factory.create(speakerId, agendaRepository, alarmScheduler)
    )
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakerUiState.Loading -> SpeakerDetail(
            speaker = (uiState.value as org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakerUiState.Loading).speaker,
            modifier = modifier,
            isLoading = true,
            onTalkClicked = {},
            onFavoriteClicked = {},
            onLinkClicked = {},
            onBackClicked = onBackClicked
        )

        is org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakerUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakerUiState.Success -> SpeakerDetail(
            speaker = (uiState.value as org.gdglille.devfest.android.theme.m3.speakers.feature.SpeakerUiState.Success).speaker,
            modifier = modifier,
            onTalkClicked = onTalkClicked,
            onFavoriteClicked = {
                viewModel.markAsFavorite(context, it)
            },
            onLinkClicked = onLinkClicked,
            onBackClicked = onBackClicked
        )
    }
}
