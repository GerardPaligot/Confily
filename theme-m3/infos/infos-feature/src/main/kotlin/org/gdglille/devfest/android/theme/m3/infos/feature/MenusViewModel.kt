package org.gdglille.devfest.android.theme.m3.infos.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.paligot.confily.core.repositories.AgendaRepository
import com.paligot.confily.models.ui.MenuItemUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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

class MenusViewModel(repository: AgendaRepository) : ViewModel() {
    val uiState: StateFlow<MenusUiState> = repository.menus()
        .map { MenusUiState.Success(it) }
        .catch {
            Firebase.crashlytics.recordException(it)
            MenusUiState.Failure(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MenusUiState.Loading(persistentListOf(MenuItemUi.fake, MenuItemUi.fake))
        )
}
