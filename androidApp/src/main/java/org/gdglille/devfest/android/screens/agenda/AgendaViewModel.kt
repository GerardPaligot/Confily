package org.gdglille.devfest.android.screens.agenda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.models.AgendaUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AgendaUiState {
    data class Loading(val agenda: AgendaUi) : AgendaUiState()
    data class Success(val agenda: AgendaUi) : AgendaUiState()
    data class Failure(val throwable: Throwable) : AgendaUiState()
}

class AgendaViewModel(
    private val repository: AgendaRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<AgendaUiState>(AgendaUiState.Loading(AgendaUi.fake))
    val uiState: StateFlow<AgendaUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                repository.fetchAndStoreAgenda()
            } catch (ignored: Throwable) {
            }
            try {
                repository.agenda().collect {
                    _uiState.value = AgendaUiState.Success(it)
                }
            } catch (ignore: Throwable) {
                _uiState.value = AgendaUiState.Failure(ignore)
            }
        }
    }

    fun markAsFavorite(scheduleId: String, isFavorite: Boolean) = viewModelScope.launch {
        repository.markAsRead(scheduleId, isFavorite)
    }

    object Factory {
        fun create(repository: AgendaRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                AgendaViewModel(repository = repository) as T
        }
    }
}
