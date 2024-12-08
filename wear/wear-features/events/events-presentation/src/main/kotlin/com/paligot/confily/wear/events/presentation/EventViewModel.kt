package com.paligot.confily.wear.events.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.events.entities.EventInfo
import com.paligot.confily.wear.events.panes.EventModelUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class EventUiState {
    data object Loading : EventUiState()
    data class Success(val modelUi: EventModelUi) : EventUiState()
}

class EventViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    val uiState = eventRepository.event()
        .map {
            if (it == null) {
                EventUiState.Loading
            } else {
                EventUiState.Success(it.info.mapToEventModelUi())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = EventUiState.Loading
        )

    init {
        viewModelScope.launch {
            eventRepository.fetchAndStoreAgenda()
        }
    }

    fun changeEvent() = viewModelScope.launch {
        eventRepository.deleteEventId()
    }
}

private fun EventInfo.mapToEventModelUi() = EventModelUi(name = name)
