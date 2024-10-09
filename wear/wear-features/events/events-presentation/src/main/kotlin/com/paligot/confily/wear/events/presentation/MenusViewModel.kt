package com.paligot.confily.wear.events.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.wear.events.panes.MenusModelUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class MenusUiState {
    data object Loading : MenusUiState()
    data object Failure : MenusUiState()
    data class Success(val modelUi: MenusModelUi) : MenusUiState()
}

class MenusViewModel(repository: EventRepository) : ViewModel() {
    val uiState = repository.menus()
        .map { MenusUiState.Success(MenusModelUi(it)) }
        .catch { MenusUiState.Failure }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MenusUiState.Loading
        )
}
