package com.paligot.confily.schedules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.paligot.confily.core.schedules.SchedulesRepository
import com.paligot.confily.models.ui.EventSessionItemUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class ScheduleEventUiState {
    data object Loading : ScheduleEventUiState()
    data class Success(val session: EventSessionItemUi) : ScheduleEventUiState()
    data class Failure(val throwable: Throwable) : ScheduleEventUiState()
}

class ScheduleDetailEventSessionViewModel(
    scheduleId: String,
    repository: SchedulesRepository
) : ViewModel() {
    val uiState: StateFlow<ScheduleEventUiState> = repository.scheduleEventSessionItem(scheduleId)
        .map { ScheduleEventUiState.Success(it) }
        .catch {
            Firebase.crashlytics.recordException(it)
            ScheduleEventUiState.Failure(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = ScheduleEventUiState.Loading,
            started = SharingStarted.WhileSubscribed()
        )
}
