package com.paligot.conferences.android.screens.agenda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.paligot.conferences.android.screens.convertToModelUi
import com.paligot.conferences.repositories.AgendaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AgendaUiState {
    object Loading : AgendaUiState()
    data class Success(val agenda: AgendaUi) : AgendaUiState()
    data class Failure(val throwable: Throwable) : AgendaUiState()
}

class AgendaViewModel(
    private val repository: AgendaRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<AgendaUiState>(AgendaUiState.Loading)
    val uiState: StateFlow<AgendaUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                _uiState.value = AgendaUiState.Success(repository.agenda().convertToModelUi())
            } catch (ignore: Throwable) {
                _uiState.value = AgendaUiState.Failure(ignore)
            }
        }
    }

    object Factory {
        fun create(repository: AgendaRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST") return AgendaViewModel(repository = repository) as T
            }
        }
    }
}
