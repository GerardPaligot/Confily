package org.gdglille.devfest.android.theme.m3.features

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import org.gdglille.devfest.android.data.AlarmScheduler
import org.gdglille.devfest.android.data.viewmodels.SpeakerUiState
import org.gdglille.devfest.android.data.viewmodels.SpeakerViewModel
import org.gdglille.devfest.android.screens.speakers.SpeakerDetail
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.AgendaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakerDetailVM(
    speakerId: String,
    agendaRepository: AgendaRepository,
    alarmScheduler: AlarmScheduler,
    onTalkClicked: (id: String) -> Unit,
    onLinkClicked: (url: String) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SpeakerViewModel = viewModel(
        factory = SpeakerViewModel.Factory.create(speakerId, agendaRepository, alarmScheduler)
    )
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is SpeakerUiState.Loading -> SpeakerDetail(
            speaker = (uiState.value as SpeakerUiState.Loading).speaker,
            modifier = modifier,
            onTalkClicked = {},
            onFavoriteClicked = {},
            onLinkClicked = {},
            onBackClicked = onBackClicked
        )

        is SpeakerUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is SpeakerUiState.Success -> SpeakerDetail(
            speaker = (uiState.value as SpeakerUiState.Success).speaker,
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
