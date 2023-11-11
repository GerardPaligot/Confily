package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import io.openfeedback.android.viewmodels.OpenFeedbackFirebaseConfig
import org.gdglille.devfest.android.theme.m3.schedules.screens.ScheduleDetailScreen
import org.gdglille.devfest.android.theme.m3.style.R
import org.gdglille.devfest.repositories.AgendaRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleDetailVM(
    scheduleId: String,
    openfeedbackFirebaseConfig: OpenFeedbackFirebaseConfig,
    agendaRepository: AgendaRepository,
    onBackClicked: () -> Unit,
    onSpeakerClicked: (id: String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScheduleDetailViewModel = viewModel(
        factory = ScheduleDetailViewModel.Factory.create(scheduleId, agendaRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is ScheduleUiState.Loading -> Text(text = stringResource(id = R.string.text_loading))
        is ScheduleUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is ScheduleUiState.Success -> ScheduleDetailScreen(
            talk = (uiState.value as ScheduleUiState.Success).talk,
            openfeedbackFirebaseConfig = openfeedbackFirebaseConfig,
            modifier = modifier,
            onBackClicked = onBackClicked,
            onSpeakerClicked = onSpeakerClicked,
            onShareClicked = onShareClicked
        )
    }
}
