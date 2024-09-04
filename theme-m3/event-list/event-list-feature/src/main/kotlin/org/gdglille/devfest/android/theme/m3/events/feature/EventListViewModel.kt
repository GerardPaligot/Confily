package org.gdglille.devfest.android.theme.m3.events.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.paligot.confily.core.repositories.EventRepository
import com.paligot.confily.models.ui.EventItemListUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class EventListUiState {
    data class Loading(val events: EventItemListUi) : EventListUiState()
    data class Success(val events: EventItemListUi) : EventListUiState()
    data class Failure(val throwable: Throwable) : EventListUiState()
}

class EventListViewModel(private val repository: EventRepository) : ViewModel() {
    val uiState: StateFlow<EventListUiState> = repository.events()
        .map { EventListUiState.Success(it) as EventListUiState }
        .catch {
            Firebase.crashlytics.recordException(it)
            emit(EventListUiState.Failure(it))
        }
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
        repository.saveEventId(eventId)
    }
}
