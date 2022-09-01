package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.gdglille.devfest.android.data.AlarmScheduler
import org.gdglille.devfest.android.data.viewmodels.AgendaUiState
import org.gdglille.devfest.android.data.viewmodels.AgendaViewModel
import org.gdglille.devfest.android.theme.vitamin.ui.screens.agenda.Agenda
import org.gdglille.devfest.repositories.AgendaRepository

@ExperimentalCoroutinesApi
@FlowPreview
@Composable
fun AgendaVM(
    agendaRepository: AgendaRepository,
    alarmScheduler: AlarmScheduler,
    modifier: Modifier = Modifier,
    onTalkClicked: (id: String) -> Unit,
) {
    val context = LocalContext.current
    val viewModel: AgendaViewModel = viewModel(
        factory = AgendaViewModel.Factory.create(agendaRepository, alarmScheduler)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is AgendaUiState.Loading -> Agenda(
            agenda = (uiState.value as AgendaUiState.Loading).agenda,
            modifier = modifier,
            isLoading = true,
            onTalkClicked = {},
            onFavoriteClicked = { }
        )

        is AgendaUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is AgendaUiState.Success -> Agenda(
            agenda = (uiState.value as AgendaUiState.Success).agenda,
            modifier = modifier,
            onTalkClicked = onTalkClicked,
            onFavoriteClicked = { talkItem ->
                viewModel.markAsFavorite(context, talkItem)
            }
        )
    }
}
