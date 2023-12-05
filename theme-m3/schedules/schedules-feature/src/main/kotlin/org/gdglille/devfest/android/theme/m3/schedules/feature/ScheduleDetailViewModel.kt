package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdglille.devfest.models.ui.TalkUi
import org.gdglille.devfest.repositories.AgendaRepository

sealed class ScheduleUiState {
    data object Loading : ScheduleUiState()
    data class Success(val talk: TalkUi) : ScheduleUiState()
    data class Failure(val throwable: Throwable) : ScheduleUiState()
}

class ScheduleDetailViewModel(
    private val scheduleId: String,
    private val repository: AgendaRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ScheduleUiState>(ScheduleUiState.Loading)
    val uiState: StateFlow<ScheduleUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                _uiState.value = ScheduleUiState.Success(repository.scheduleItem(scheduleId))
            } catch (error: Throwable) {
                Firebase.crashlytics.recordException(error)
                _uiState.value = ScheduleUiState.Failure(error)
            }
        }
    }
}
