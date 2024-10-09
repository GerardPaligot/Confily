package com.paligot.confily.wear.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.events.EventRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class MainUiState {
    data object Loading : MainUiState()
    data class Success(val startDestination: String) : MainUiState()
}

class MainViewModel(repository: EventRepository) : ViewModel() {
    val uiState = flow { emit(repository.isInitialized()) }
        .map { MainUiState.Success(if (it) "event" else "events") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MainUiState.Loading
        )
}
