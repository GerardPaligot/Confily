package com.paligot.confily.wear.presentation.schedules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.schedules.SchedulesRepository
import com.paligot.confily.models.ui.TalkUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class ScheduleUiState {
    data object Loading : ScheduleUiState()
    data class Success(val modelUi: TalkUi) : ScheduleUiState()
}

class ScheduleViewModel(sessionId: String, repository: SchedulesRepository) : ViewModel() {
    val uiState = repository.scheduleItem(sessionId)
        .map { ScheduleUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ScheduleUiState.Loading
        )
}
