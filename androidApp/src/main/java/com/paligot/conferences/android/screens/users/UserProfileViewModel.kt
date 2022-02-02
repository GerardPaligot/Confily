package com.paligot.conferences.android.screens.users

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.paligot.conferences.android.asImageBitmap
import com.paligot.conferences.repositories.UserProfileUi
import com.paligot.conferences.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val profile: UserProfileUi, val imageBitmap: ImageBitmap?) : ProfileUiState()
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
                    showQrCode = false
                ),
                imageBitmap = image?.second?.asImageBitmap()
            )
            userRepository.fetchNetworking().collect {
                if (_uiState.value !is ProfileUiState.Success) return@collect
                _uiState.value = ProfileUiState.Success(
                    profile = (_uiState.value as ProfileUiState.Success).profile.copy(emails = it),
                    imageBitmap = (_uiState.value as ProfileUiState.Success).imageBitmap
                )
            }
        }
    }

    fun emailChanged(input: String) {
        if (_uiState.value !is ProfileUiState.Success) return
        _uiState.value = ProfileUiState.Success(
            profile = (_uiState.value as ProfileUiState.Success).profile.copy(email = input),
            imageBitmap = (_uiState.value as ProfileUiState.Success).imageBitmap
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
                showQrCode = false
            ),
            imageBitmap = image.asImageBitmap()
        )
    }

    fun displayQrCode() {
        if (_uiState.value !is ProfileUiState.Success) return
        val success = _uiState.value as ProfileUiState.Success
        if (success.imageBitmap == null) return
        _uiState.value = ProfileUiState.Success(
            profile = success.profile.copy(showQrCode = true),
            imageBitmap = success.imageBitmap
        )
    }

    fun closeQrCode() {
        if (_uiState.value !is ProfileUiState.Success) return
        val success = _uiState.value as ProfileUiState.Success
        _uiState.value = ProfileUiState.Success(
            profile = success.profile.copy(showQrCode = false),
            imageBitmap = success.imageBitmap
        )
    }

    object Factory {
        fun create(repository: UserRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                UserProfileViewModel(userRepository = repository) as T
        }
    }
}