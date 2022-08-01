package org.gdglille.devfest.android.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdglille.devfest.models.EventUi
import org.gdglille.devfest.repositories.AgendaRepository

sealed class EventUiState {
    data class Loading(val event: EventUi) : EventUiState()
    data class Success(val event: EventUi) : EventUiState()
    data class Failure(val throwable: Throwable) : EventUiState()
}

class EventViewModel(private val repository: AgendaRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<EventUiState>(EventUiState.Loading(EventUi.fake))
    val uiState: StateFlow<EventUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                repository.event().collect {
                    _uiState.value = EventUiState.Success(it)
                }
            } catch (error: Throwable) {
                Firebase.crashlytics.recordException(error)
                _uiState.value = EventUiState.Failure(error)
            }
        }
    }

    object Factory {
        fun create(repository: AgendaRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = EventViewModel(repository = repository) as T
        }
    }
}
