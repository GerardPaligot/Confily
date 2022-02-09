package com.paligot.conferences.android.screens.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.paligot.conferences.repositories.AgendaRepository
import com.paligot.conferences.models.EventUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
                _uiState.value = EventUiState.Success(repository.event())
            } catch (ignore: Throwable) {
                _uiState.value = EventUiState.Failure(ignore)
            }
        }
    }

    object Factory {
        fun create(repository: AgendaRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T = EventViewModel(repository = repository) as T
        }
    }
}
