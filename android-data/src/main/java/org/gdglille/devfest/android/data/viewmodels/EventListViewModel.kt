package org.gdglille.devfest.android.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gdglille.devfest.models.EventItemListUi
import org.gdglille.devfest.repositories.EventRepository

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

    object Factory {
        fun create(repository: EventRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                EventListViewModel(repository = repository) as T
        }
    }
}
