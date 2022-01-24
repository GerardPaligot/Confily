package com.paligot.conferences.android.screens.speakers

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paligot.conferences.repositories.AgendaRepository
import com.paligot.conferences.ui.screens.SpeakerDetail

@Composable
fun SpeakerDetailVM(
    speakerId: String,
    agendaRepository: AgendaRepository,
    modifier: Modifier = Modifier,
    onTwitterClick: (url: String) -> Unit,
    onGitHubClick: (url: String) -> Unit,
    onBackClicked: () -> Unit
) {
    val viewModel: SpeakerViewModel = viewModel(
        factory = SpeakerViewModel.Factory.create(speakerId, agendaRepository)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is SpeakerUiState.Loading -> Text("Loading...")
        is SpeakerUiState.Failure -> Text("Something wrong happened")
        is SpeakerUiState.Success -> SpeakerDetail(
            speaker = (uiState.value as SpeakerUiState.Success).speaker,
            modifier = modifier,
            onTwitterClick = onTwitterClick,
            onGitHubClick = onGitHubClick,
            onBackClicked = onBackClicked
        )
    }
}
