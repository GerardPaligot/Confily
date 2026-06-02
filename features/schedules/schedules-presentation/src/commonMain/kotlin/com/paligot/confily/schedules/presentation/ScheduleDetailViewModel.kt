package com.paligot.confily.schedules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.core.schedules.entities.mapToSessionUi
import com.paligot.confily.resources.Strings
import com.paligot.confily.schedules.panes.models.SessionUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

sealed class ScheduleUiState {
    data object Loading : ScheduleUiState()
    data class Success(val talk: SessionUi) : ScheduleUiState()
    data class Failure(val throwable: Throwable) : ScheduleUiState()
}

class ScheduleDetailViewModel(
    scheduleId: String,
    repository: SessionRepository,
    strings: Strings
) : ViewModel() {
    val uiState: StateFlow<ScheduleUiState> = combine(
        repository.session(scheduleId),
        repository.featureFlags()
    ) { session, flags ->
        ScheduleUiState.Success(
            session.mapToSessionUi(strings, flags.openFeedbackEnabled)
        ) as ScheduleUiState
    }
        .catch { emit(ScheduleUiState.Failure(it)) }
        .stateIn(
            scope = viewModelScope,
            initialValue = ScheduleUiState.Loading,
            started = SharingStarted.WhileSubscribed()
        )
}
