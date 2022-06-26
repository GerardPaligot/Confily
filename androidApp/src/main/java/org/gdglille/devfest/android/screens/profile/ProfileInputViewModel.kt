package org.gdglille.devfest.android.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdglille.devfest.android.components.users.Field
import org.gdglille.devfest.models.UserProfileUi
import org.gdglille.devfest.repositories.UserRepository

sealed class ProfileInputUiState {
    object Loading : ProfileInputUiState()
    data class Success(val profile: UserProfileUi) : ProfileInputUiState()
    data class Failure(val throwable: Throwable) : ProfileInputUiState()
}

class ProfileInputViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileInputUiState>(ProfileInputUiState.Loading)
    val uiState: StateFlow<ProfileInputUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                val profile = userRepository.fetchProfile()
                _uiState.value = ProfileInputUiState.Success(
                    profile = profile ?: UserProfileUi(
                        email = "",
                        firstName = "",
                        lastName = "",
                        company = "",
                        qrCode = null
                    )
                )
            } catch (error: Throwable) {
                Firebase.crashlytics.recordException(error)
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
        val profileDb = userRepository.saveProfile(
            email,
            profile.firstName,
            profile.lastName,
            profile.company
        )
        _uiState.value = ProfileInputUiState.Success(profile = profileDb)
    }

    object Factory {
        fun create(repository: UserRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                ProfileInputViewModel(userRepository = repository) as T
        }
    }
}
