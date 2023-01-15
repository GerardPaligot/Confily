package org.gdglille.devfest.android.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.gdglille.devfest.repositories.EventRepository

sealed class MainUiState {
    object Loading : MainUiState()
    data class Success(val initialized: Boolean) : MainUiState()
}

class MainViewModel(private val repository: EventRepository) : ViewModel() {
    val uiState: StateFlow<MainUiState> = flow { emit(repository.isInitialized()) }
        .map { MainUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MainUiState.Loading
        )

    object Factory {
        fun create(repository: EventRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel(repository = repository) as T
        }
    }
}
