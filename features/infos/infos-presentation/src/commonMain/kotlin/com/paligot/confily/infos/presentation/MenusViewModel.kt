package com.paligot.confily.infos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.events.entities.mapToMenuItemUi
import com.paligot.confily.models.ui.MenuItemUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class MenusUiState {
    data class Loading(val menus: ImmutableList<MenuItemUi>) : MenusUiState()
    data class Success(val menus: ImmutableList<MenuItemUi>) : MenusUiState()
    data class Failure(val throwable: Throwable) : MenusUiState()
}

class MenusViewModel(repository: EventRepository) : ViewModel() {
    val uiState: StateFlow<MenusUiState> = repository.menus()
        .map { MenusUiState.Success(it.map { it.mapToMenuItemUi() }.toImmutableList()) as MenusUiState }
        .catch { emit(MenusUiState.Failure(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MenusUiState.Loading(persistentListOf(MenuItemUi.fake, MenuItemUi.fake))
        )
}
