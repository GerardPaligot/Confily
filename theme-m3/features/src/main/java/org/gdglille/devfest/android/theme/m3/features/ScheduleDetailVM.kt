package org.gdglille.devfest.android.theme.m3.features

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import io.openfeedback.android.OpenFeedbackConfig
import org.gdglille.devfest.android.data.viewmodels.ScheduleItemViewModel
import org.gdglille.devfest.android.data.viewmodels.ScheduleUiState
import org.gdglille.devfest.android.screens.agenda.ScheduleDetail
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.AgendaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDetailVM(
    scheduleId: String,
    openFeedbackState: OpenFeedbackConfig,
    agendaRepository: AgendaRepository,
    onBackClicked: () -> Unit,
    onSpeakerClicked: (id: String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScheduleItemViewModel = viewModel(
        factory = ScheduleItemViewModel.Factory.create(scheduleId, agendaRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is ScheduleUiState.Loading -> Text(text = stringResource(id = R.string.text_loading))
        is ScheduleUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is ScheduleUiState.Success -> ScheduleDetail(
            talk = (uiState.value as ScheduleUiState.Success).talk,
            openFeedbackState = openFeedbackState,
            modifier = modifier,
            onBackClicked = onBackClicked,
            onSpeakerClicked = onSpeakerClicked,
            onShareClicked = onShareClicked
        )
    }
}
