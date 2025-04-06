package com.paligot.confily.events.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.events.entities.mapToEventItemListUi
import com.paligot.confily.events.ui.models.EventItemListUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class EventListUiState {
    data class Loading(val events: EventItemListUi) : EventListUiState()
    data class Success(val events: EventItemListUi) : EventListUiState()
    data class Failure(val throwable: Throwable) : EventListUiState()
}

class EventListViewModel(private val repository: EventRepository) : ViewModel() {
    private val _loadEvent = MutableStateFlow<Boolean?>(value = null)
    val loadEvent: StateFlow<Boolean?> = _loadEvent
    val uiState: StateFlow<EventListUiState> = repository.events()
        .map { EventListUiState.Success(it.mapToEventItemListUi()) as EventListUiState }
        .catch { emit(EventListUiState.Failure(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = EventListUiState.Loading(EventItemListUi.fake)
        )

    init {
        viewModelScope.launch {
            repository.fetchAndStoreEventList()
        }
    }

    fun savedEventId(eventId: String) = viewModelScope.launch {
        _loadEvent.update { true }
        repository.saveEventId(eventId)
        repository.fetchAndStoreAgenda()
        _loadEvent.update { false }
    }
}
