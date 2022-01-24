package com.paligot.conferences.android.screens.schedule

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.paligot.conferences.repositories.AgendaRepository
import com.paligot.conferences.ui.screens.ScheduleDetail

@Composable
fun ScheduleDetailVM(
    scheduleId: String,
    agendaRepository: AgendaRepository,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onSpeakerClicked: (id: String) -> Unit
) {
    val viewModel: ScheduleItemViewModel = viewModel(
        factory = ScheduleItemViewModel.Factory.create(scheduleId, agendaRepository)
    )
    val uiState = viewModel.uiState.collectAsState()
    when (uiState.value) {
        is ScheduleUiState.Loading -> Text("Loading...")
        is ScheduleUiState.Failure -> Text("Something wrong happened")
        is ScheduleUiState.Success -> ScheduleDetail(
            talk = (uiState.value as ScheduleUiState.Success).talk,
            modifier = modifier,
            onBackClicked = onBackClicked,
            onSpeakerClicked = onSpeakerClicked
        )
    }
}
