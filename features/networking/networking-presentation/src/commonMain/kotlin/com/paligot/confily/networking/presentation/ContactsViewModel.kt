package com.paligot.confily.networking.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.networking.NetworkingRepository
import com.paligot.confily.models.ui.UserNetworkingUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ContactsUiState {
    data object Loading : ContactsUiState()
    data class Success(val users: ImmutableList<UserNetworkingUi>) : ContactsUiState()
    data class Failure(val throwable: Throwable) : ContactsUiState()
}

class ContactsViewModel(
    private val repository: NetworkingRepository
) : ViewModel() {
    val uiState: StateFlow<ContactsUiState> = repository.fetchNetworking()
        .map { ContactsUiState.Success(users = it) as ContactsUiState }
        .catch { emit(ContactsUiState.Failure(it)) }
        .stateIn(
            scope = viewModelScope,
            initialValue = ContactsUiState.Loading,
            started = SharingStarted.WhileSubscribed()
        )

    fun deleteNetworking(email: String) = viewModelScope.launch {
        repository.deleteNetworkProfile(email)
    }
}
