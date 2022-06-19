package org.gdglille.devfest.android.screens.speakers

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.R
import org.gdglille.devfest.android.screens.SpeakerDetail
import org.gdglille.devfest.repositories.AgendaRepository

@ExperimentalMaterial3Api
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
        is SpeakerUiState.Loading -> Text(text = stringResource(id = R.string.text_loading))
        is SpeakerUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is SpeakerUiState.Success -> SpeakerDetail(
            speaker = (uiState.value as SpeakerUiState.Success).speaker,
            modifier = modifier,
            onTwitterClick = onTwitterClick,
            onGitHubClick = onGitHubClick,
            onBackClicked = onBackClicked
        )
    }
}
