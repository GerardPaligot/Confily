package org.gdglille.devfest.android.theme.m2.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.AlarmScheduler
import org.gdglille.devfest.android.data.viewmodels.SpeakerUiState
import org.gdglille.devfest.android.data.viewmodels.SpeakerViewModel
import org.gdglille.devfest.android.ui.m2.screens.SpeakerDetail
import org.gdglille.devfest.repositories.AgendaRepository

@Composable
fun SpeakerDetailVM(
    speakerId: String,
    agendaRepository: AgendaRepository,
    alarmScheduler: AlarmScheduler,
    modifier: Modifier = Modifier,
    onLinkClicked: (url: String) -> Unit,
    onBackClicked: () -> Unit
) {
    val viewModel: SpeakerViewModel = viewModel(
        factory = SpeakerViewModel.Factory.create(speakerId, agendaRepository, alarmScheduler)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is SpeakerUiState.Loading -> SpeakerDetail(
            speaker = (uiState.value as SpeakerUiState.Loading).speaker,
            modifier = modifier,
            onLinkClicked = {},
            onBackClicked = onBackClicked
        )

        is SpeakerUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is SpeakerUiState.Success -> SpeakerDetail(
            speaker = (uiState.value as SpeakerUiState.Success).speaker,
            modifier = modifier,
            onLinkClicked = onLinkClicked,
            onBackClicked = onBackClicked
        )
    }
}
