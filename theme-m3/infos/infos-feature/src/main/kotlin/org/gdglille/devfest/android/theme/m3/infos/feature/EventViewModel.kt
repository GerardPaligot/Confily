package org.gdglille.devfest.android.theme.m3.infos.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.paligot.confily.core.repositories.AgendaRepository
import com.paligot.confily.models.ui.EventUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class EventUiState {
    data class Loading(val event: EventUi) : EventUiState()
    data class Success(val event: EventUi) : EventUiState()
    data class Failure(val throwable: Throwable) : EventUiState()
}

class EventViewModel(repository: AgendaRepository) : ViewModel() {
    val uiState: StateFlow<EventUiState> = repository.event()
        .map { EventUiState.Success(it) }
        .catch {
            Firebase.crashlytics.recordException(it)
            EventUiState.Failure(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = EventUiState.Loading(EventUi.fake)
        )
}
