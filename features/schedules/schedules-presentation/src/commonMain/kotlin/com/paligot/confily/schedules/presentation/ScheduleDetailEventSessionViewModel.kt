package com.paligot.confily.schedules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.core.schedules.entities.mapToUi
import com.paligot.confily.core.speakers.entities.mapToUi
import com.paligot.confily.models.ui.EventSessionUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class ScheduleEventUiState {
    data object Loading : ScheduleEventUiState()
    data class Success(val session: EventSessionUi) : ScheduleEventUiState()
    data class Failure(val throwable: Throwable) : ScheduleEventUiState()
}

class ScheduleDetailEventSessionViewModel(
    scheduleId: String,
    repository: SessionRepository
) : ViewModel() {
    val uiState: StateFlow<ScheduleEventUiState> = repository.eventSession(scheduleId)
        .map { ScheduleEventUiState.Success(it.mapToUi()) as ScheduleEventUiState }
        .catch { emit(ScheduleEventUiState.Failure(it)) }
        .stateIn(
            scope = viewModelScope,
            initialValue = ScheduleEventUiState.Loading,
            started = SharingStarted.WhileSubscribed()
        )
}
