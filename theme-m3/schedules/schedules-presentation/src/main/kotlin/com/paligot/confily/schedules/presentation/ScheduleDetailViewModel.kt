package com.paligot.confily.schedules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.paligot.confily.core.repositories.AgendaRepository
import com.paligot.confily.models.ui.TalkUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class ScheduleUiState {
    data object Loading : ScheduleUiState()
    data class Success(val talk: TalkUi) : ScheduleUiState()
    data class Failure(val throwable: Throwable) : ScheduleUiState()
}

class ScheduleDetailViewModel(
    scheduleId: String,
    repository: AgendaRepository
) : ViewModel() {
    val uiState: StateFlow<ScheduleUiState> = repository.scheduleItem(scheduleId)
        .map { ScheduleUiState.Success(it) }
        .catch {
            Firebase.crashlytics.recordException(it)
            ScheduleUiState.Failure(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = ScheduleUiState.Loading,
            started = SharingStarted.WhileSubscribed()
        )
}
