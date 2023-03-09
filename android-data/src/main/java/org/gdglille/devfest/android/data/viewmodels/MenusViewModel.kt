package org.gdglille.devfest.android.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdglille.devfest.models.MenuItemUi
import org.gdglille.devfest.repositories.AgendaRepository

sealed class MenusUiState {
    data class Loading(val menus: ImmutableList<MenuItemUi>) : MenusUiState()
    data class Success(val menus: ImmutableList<MenuItemUi>) : MenusUiState()
    data class Failure(val throwable: Throwable) : MenusUiState()
}

class MenusViewModel(private val repository: AgendaRepository) : ViewModel() {
    private val _uiState =
        MutableStateFlow<MenusUiState>(
            MenusUiState.Loading(
                persistentListOf(
                    MenuItemUi.fake,
                    MenuItemUi.fake
                )
            )
        )
    val uiState: StateFlow<MenusUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                repository.menus().collect {
                    _uiState.value = MenusUiState.Success(it)
                }
            } catch (error: Throwable) {
                Firebase.crashlytics.recordException(error)
                _uiState.value = MenusUiState.Failure(error)
            }
        }
    }

    object Factory {
        fun create(repository: AgendaRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MenusViewModel(repository = repository) as T
        }
    }
}
