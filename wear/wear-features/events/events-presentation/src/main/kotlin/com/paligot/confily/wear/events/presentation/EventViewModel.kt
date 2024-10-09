package com.paligot.confily.wear.events.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.agenda.AgendaRepository
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.models.ui.EventInfoUi
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
    agendaRepository: AgendaRepository,
    private val eventRepository: EventRepository
) : ViewModel() {
    val uiState = eventRepository.event()
        .map {
            if (it == null) {
                EventUiState.Loading
            } else {
                EventUiState.Success(it.toModelUi())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = EventUiState.Loading
        )

    init {
        viewModelScope.launch {
            agendaRepository.fetchAndStoreAgenda()
        }
    }

    fun changeEvent() = viewModelScope.launch {
        eventRepository.deleteEventId()
    }
}

private fun EventInfoUi.toModelUi() = EventModelUi(name = name)
