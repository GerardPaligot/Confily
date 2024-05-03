package org.gdglille.devfest.android.widgets.feature

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.gdglille.devfest.models.ui.UserProfileUi
import org.gdglille.devfest.repositories.UserRepository

sealed class NetworkingUiState {
    data object Loading : NetworkingUiState()
    data class Success(val user: UserProfileUi?) : NetworkingUiState()
}

class NetworkingViewModel(
    userRepository: UserRepository,
    coroutineScope: CoroutineScope = CoroutineScope(Job())
) {
    val uiState: StateFlow<NetworkingUiState> = userRepository.fetchProfile()
        .map { NetworkingUiState.Success(it) }
        .catch { emit(NetworkingUiState.Success(null)) }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NetworkingUiState.Loading
        )
}
