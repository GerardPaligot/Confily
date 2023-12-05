package org.gdglille.devfest.android.theme.m3.networking.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.gdglille.devfest.models.ui.UserProfileUi
import org.gdglille.devfest.repositories.UserRepository

sealed class MyProfileUiState {
    data object Loading : MyProfileUiState()
    data class Success(val profile: UserProfileUi) : MyProfileUiState()
    data class Failure(val throwable: Throwable) : MyProfileUiState()
}

class MyProfileViewModel(userRepository: UserRepository) : ViewModel() {
    val uiState: StateFlow<MyProfileUiState> = userRepository.fetchProfile()
        .map {
            MyProfileUiState.Success(
                profile = it ?: UserProfileUi(
                    email = "",
                    firstName = "",
                    lastName = "",
                    company = "",
                    qrCode = null
                )
            )
        }
        .catch {
            Firebase.crashlytics.recordException(it)
            MyProfileUiState.Failure(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = MyProfileUiState.Loading,
            started = SharingStarted.WhileSubscribed()
        )
}
