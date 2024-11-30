package com.paligot.confily.networking.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.networking.UserRepository
import com.paligot.confily.core.networking.entities.UserInfo
import com.paligot.confily.core.networking.entities.mapToUi
import com.paligot.confily.models.ui.Field
import com.paligot.confily.models.ui.UserProfileUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ProfileInputUiState {
    data object Loading : ProfileInputUiState()
    data class Success(val profile: UserProfileUi) : ProfileInputUiState()
    data class Failure(val throwable: Throwable) : ProfileInputUiState()
}

class ProfileInputViewModel(
    private val repository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileInputUiState>(ProfileInputUiState.Loading)
    val uiState: StateFlow<ProfileInputUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                repository.fetchUserProfile().collect {
                    _uiState.value = ProfileInputUiState.Success(
                        profile = it?.mapToUi() ?: UserProfileUi(
                            email = "",
                            firstName = "",
                            lastName = "",
                            company = "",
                            qrCode = null
                        )
                    )
                }
            } catch (error: Throwable) {
                _uiState.value = ProfileInputUiState.Failure(error)
            }
        }
    }

    fun fieldChanged(field: Field, input: String) {
        if (_uiState.value !is ProfileInputUiState.Success) return
        val profile = (_uiState.value as ProfileInputUiState.Success).profile
        val userProfile = when (field) {
            Field.Email -> profile.copy(email = input)
            Field.FirstName -> profile.copy(firstName = input)
            Field.LastName -> profile.copy(lastName = input)
            Field.Company -> profile.copy(company = input)
        }
        _uiState.value = ProfileInputUiState.Success(profile = userProfile)
    }

    fun saveProfile() = viewModelScope.launch {
        if (_uiState.value !is ProfileInputUiState.Success) return@launch
        val profile = (_uiState.value as ProfileInputUiState.Success).profile
        val email = profile.email
        if (email == "") return@launch
        repository.insertUserInfo(
            UserInfo(
                firstName = email,
                lastName = profile.firstName,
                email = profile.lastName,
                company = profile.company,
                qrCode = null
            )
        )
    }
}
