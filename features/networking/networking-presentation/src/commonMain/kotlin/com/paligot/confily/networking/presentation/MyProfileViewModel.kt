package com.paligot.confily.networking.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.networking.UserRepository
import com.paligot.confily.core.networking.entities.mapToUi
import com.paligot.confily.models.ui.UserProfileUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class MyProfileUiState {
    data object Loading : MyProfileUiState()
    data class Success(val profile: UserProfileUi) : MyProfileUiState()
    data class Failure(val throwable: Throwable) : MyProfileUiState()
}

class MyProfileViewModel(repository: UserRepository) : ViewModel() {
    val uiState: StateFlow<MyProfileUiState> = repository.fetchUserProfile()
        .map {
            MyProfileUiState.Success(
                profile = it?.mapToUi() ?: UserProfileUi(
                    email = "",
                    firstName = "",
                    lastName = "",
                    company = "",
                    qrCode = null
                )
            ) as MyProfileUiState
        }
        .catch { emit(MyProfileUiState.Failure(it)) }
        .stateIn(
            scope = viewModelScope,
            initialValue = MyProfileUiState.Loading,
            started = SharingStarted.WhileSubscribed()
        )
}
