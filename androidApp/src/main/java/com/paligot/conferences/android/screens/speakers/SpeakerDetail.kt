package com.paligot.conferences.android.screens.speakers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paligot.conferences.android.components.speakers.SpeakerHeader
import com.paligot.conferences.android.components.speakers.SpeakerSection
import com.paligot.conferences.android.components.speakers.speaker
import com.paligot.conferences.android.theme.Conferences4HallTheme
import com.paligot.conferences.repositories.AgendaRepository
import com.paligot.conferences.repositories.SpeakerUi

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

@Composable
fun SpeakerDetail(
  speaker: SpeakerUi,
  modifier: Modifier = Modifier,
  onTwitterClick: (url: String) -> Unit,
  onGitHubClick: (url: String) -> Unit,
  onBackClicked: () -> Unit
) {
  Scaffold(
    modifier = modifier,
    content = {
      LazyColumn(
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        item {
          SpeakerHeader(
            url = speaker.url,
            name = speaker.name,
            company = speaker.company,
            onBackClicked = onBackClicked
          )
        }
        item {
          SpeakerSection(
            speaker = speaker,
            onTwitterClick = onTwitterClick,
            onGitHubClick = onGitHubClick
          )
        }
      }
    }
  )
}

@Preview
@Composable
fun SpeakerListPreview() {
  Conferences4HallTheme {
    SpeakerDetail(
      speaker = speaker,
      onTwitterClick = {},
      onGitHubClick = {},
      onBackClicked = {}
    )
  }
}
