package com.paligot.conferences.android.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.paligot.conferences.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class NetworkingUiState {
    object Loading : NetworkingUiState()
    data class Success(val emails: List<String>) : NetworkingUiState()
    data class Failure(val throwable: Throwable) : NetworkingUiState()
}

class NetworkingViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<NetworkingUiState>(NetworkingUiState.Loading)
    val uiState: StateFlow<NetworkingUiState> = _uiState

    init {
        viewModelScope.launch {
            userRepository.fetchNetworking().collect {
                if (_uiState.value !is NetworkingUiState.Success) return@collect
                _uiState.value = NetworkingUiState.Success(emails = it)
            }
        }
    }

    object Factory {
        fun create(repository: UserRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                NetworkingViewModel(userRepository = repository) as T
        }
    }
}