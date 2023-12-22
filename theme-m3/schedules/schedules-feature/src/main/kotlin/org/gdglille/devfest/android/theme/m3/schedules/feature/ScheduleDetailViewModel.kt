package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.gdglille.devfest.models.ui.TalkUi
import org.gdglille.devfest.repositories.AgendaRepository

sealed class ScheduleUiState {
    data object Loading : ScheduleUiState()
    data class Success(val talk: TalkUi) : ScheduleUiState()
    data class Failure(val throwable: Throwable) : ScheduleUiState()
}

class ScheduleDetailViewModel(
    scheduleId: String,
    repository: AgendaRepository,
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
