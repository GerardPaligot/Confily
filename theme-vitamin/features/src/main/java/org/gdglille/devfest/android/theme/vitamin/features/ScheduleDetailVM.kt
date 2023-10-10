package org.gdglille.devfest.android.theme.vitamin.features

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import io.openfeedback.android.OpenFeedbackConfig
import org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleItemViewModel
import org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleUiState
import org.gdglille.devfest.android.theme.vitamin.ui.screens.agenda.ScheduleDetail
import org.gdglille.devfest.android.ui.resources.R
import org.gdglille.devfest.repositories.AgendaRepository

@Composable
fun ScheduleDetailVM(
    scheduleId: String,
    openFeedbackState: OpenFeedbackConfig,
    agendaRepository: AgendaRepository,
    onBackClicked: () -> Unit,
    onSpeakerClicked: (id: String) -> Unit,
    onShareClicked: (text: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleItemViewModel = viewModel(
        factory = org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleItemViewModel.Factory.create(scheduleId, agendaRepository)
    )
) {
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleUiState.Loading -> Text(text = stringResource(id = R.string.text_loading))
        is org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleUiState.Failure -> Text(text = stringResource(id = R.string.text_error))
        is org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleUiState.Success -> ScheduleDetail(
            talk = (uiState.value as org.gdglille.devfest.android.theme.m3.schedules.feature.ScheduleUiState.Success).talk,
            openFeedbackState = openFeedbackState,
            modifier = modifier,
            onBackClicked = onBackClicked,
            onSpeakerClicked = onSpeakerClicked,
            onShareClicked = onShareClicked
        )
    }
}
