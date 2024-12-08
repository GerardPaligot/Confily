package com.paligot.confily.infos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.events.entities.mapToEventInfoUi
import com.paligot.confily.core.networking.entities.mapToTicketUi
import com.paligot.confily.models.ui.EventUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

sealed class EventUiState {
    data class Loading(val event: EventUi) : EventUiState()
    data class Success(val event: EventUi) : EventUiState()
    data class Failure(val throwable: Throwable) : EventUiState()
}

class EventViewModel(repository: EventRepository) : ViewModel() {
    val uiState: StateFlow<EventUiState> = combine(
        flow = repository.event(),
        flow2 = repository.ticket(),
        transform = { event, ticket ->
            if (event == null) {
                return@combine EventUiState.Failure(NullPointerException("Event not found"))
            }
            EventUiState.Success(
                EventUi(eventInfo = event.mapToEventInfoUi(), ticket = ticket?.mapToTicketUi())
            )
        }
    ).catch {
        emit(EventUiState.Failure(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = EventUiState.Loading(EventUi.fake)
    )
}
