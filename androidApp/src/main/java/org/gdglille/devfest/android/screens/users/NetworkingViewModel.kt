package org.gdglille.devfest.android.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdglille.devfest.models.UserNetworkingUi
import org.gdglille.devfest.repositories.UserRepository

sealed class NetworkingUiState {
    object Loading : NetworkingUiState()
    data class Success(val users: List<UserNetworkingUi>) : NetworkingUiState()
    data class Failure(val throwable: Throwable) : NetworkingUiState()
}

class NetworkingViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<NetworkingUiState>(NetworkingUiState.Loading)
    val uiState: StateFlow<NetworkingUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                userRepository.fetchNetworking().collect {
                    _uiState.value = NetworkingUiState.Success(users = it)
                }
            } catch (error: Throwable) {
                Firebase.crashlytics.recordException(error)
                _uiState.value = NetworkingUiState.Failure(throwable = error)
            }
        }
    }

    fun deleteNetworking(email: String) = viewModelScope.launch {
        userRepository.deleteNetworkProfile(email)
    }

    object Factory {
        fun create(repository: UserRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                NetworkingViewModel(userRepository = repository) as T
        }
    }
}