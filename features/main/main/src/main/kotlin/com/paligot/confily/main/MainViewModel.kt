package com.paligot.confily.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.events.routes.EventList
import com.paligot.confily.schedules.routes.ScheduleList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.reflect.KClass

sealed class MainUiState {
    data object Loading : MainUiState()
    data class Success(val startDestination: KClass<*>) : MainUiState()
}

class MainViewModel(defaultEvent: String?, repository: EventRepository) : ViewModel() {
    val uiState: StateFlow<MainUiState> = flow { emit(repository.isInitialized(defaultEvent)) }
        .map { MainUiState.Success(if (it) ScheduleList::class else EventList::class) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MainUiState.Loading
        )
}
