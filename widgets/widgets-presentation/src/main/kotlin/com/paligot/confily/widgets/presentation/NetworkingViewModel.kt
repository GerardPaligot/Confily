package com.paligot.confily.widgets.presentation

import com.paligot.confily.core.networking.UserRepository
import com.paligot.confily.core.networking.entities.mapToUserProfileUi
import com.paligot.confily.models.ui.UserProfileUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class NetworkingUiState {
    data object Loading : NetworkingUiState()
    data class Success(val user: UserProfileUi?) : NetworkingUiState()
}

class NetworkingViewModel(
    userRepository: UserRepository,
    coroutineScope: CoroutineScope = CoroutineScope(Job())
) {
    val uiState: StateFlow<NetworkingUiState> = userRepository.fetchUserProfile()
        .map { NetworkingUiState.Success(it?.mapToUserProfileUi()) }
        .catch { emit(NetworkingUiState.Success(null)) }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NetworkingUiState.Loading
        )
}
