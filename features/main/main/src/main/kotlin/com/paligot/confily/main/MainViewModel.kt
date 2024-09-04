package com.paligot.confily.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.repositories.EventRepository
import com.paligot.confily.navigation.Screen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class MainUiState {
    data object Loading : MainUiState()
    data class Success(val startDestination: String) : MainUiState()
}

class MainViewModel(
    defaultEvent: String?,
    private val repository: EventRepository
) : ViewModel() {
    val uiState: StateFlow<MainUiState> = flow { emit(repository.isInitialized(defaultEvent)) }
        .map { MainUiState.Success(if (it) Screen.ScheduleList.route else Screen.EventList.route) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MainUiState.Loading
        )
}
