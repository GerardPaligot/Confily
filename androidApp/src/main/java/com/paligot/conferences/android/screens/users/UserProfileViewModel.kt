package com.paligot.conferences.android.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.paligot.conferences.models.UserProfileUi
import com.paligot.conferences.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val profile: UserProfileUi) : ProfileUiState()
    data class Failure(val throwable: Throwable) : ProfileUiState()
}

class UserProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        viewModelScope.launch {
            val image = userRepository.fetchEmailQrCode()
            _uiState.value = ProfileUiState.Success(
                profile = UserProfileUi(
                    email = image?.first ?: "",
                    hasQrCode = image?.second != null,
                    showQrCode = false,
                    qrcode = image?.second
                )
            )
            userRepository.fetchNetworking().collect {
                if (_uiState.value !is ProfileUiState.Success) return@collect
                _uiState.value = ProfileUiState.Success(
                    profile = (_uiState.value as ProfileUiState.Success).profile.copy(emails = it)
                )
            }
        }
    }

    fun emailChanged(input: String) {
        if (_uiState.value !is ProfileUiState.Success) return
        _uiState.value = ProfileUiState.Success(
            profile = (_uiState.value as ProfileUiState.Success).profile.copy(email = input)
        )
    }

    fun fetchNewEmailQrCode() = viewModelScope.launch {
        if (_uiState.value !is ProfileUiState.Success) return@launch
        val email = (_uiState.value as ProfileUiState.Success).profile.email
        if (email == "") return@launch
        val image = userRepository.fetchEmailQrCode(email)
        _uiState.value = ProfileUiState.Success(
            profile = (_uiState.value as ProfileUiState.Success).profile.copy(
                email = email,
                hasQrCode = true,
                showQrCode = false,
                qrcode = image
            )
        )
    }

    fun displayQrCode() {
        if (_uiState.value !is ProfileUiState.Success) return
        val success = _uiState.value as ProfileUiState.Success
        if (success.profile.qrcode == null) return
        _uiState.value = ProfileUiState.Success(
            profile = success.profile.copy(showQrCode = true)
        )
    }

    fun closeQrCode() {
        if (_uiState.value !is ProfileUiState.Success) return
        val success = _uiState.value as ProfileUiState.Success
        _uiState.value = ProfileUiState.Success(
            profile = success.profile.copy(showQrCode = false)
        )
    }

    object Factory {
        fun create(repository: UserRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                UserProfileViewModel(userRepository = repository) as T
        }
    }
}