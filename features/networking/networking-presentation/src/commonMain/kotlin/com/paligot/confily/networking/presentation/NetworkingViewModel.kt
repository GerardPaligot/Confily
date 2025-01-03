package com.paligot.confily.networking.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.networking.UserRepository
import com.paligot.confily.core.networking.entities.mapToExportNetworkingUi
import com.paligot.confily.navigation.FabActions
import com.paligot.confily.navigation.TabActions
import com.paligot.confily.navigation.TopActions
import com.paligot.confily.networking.routes.Contacts
import com.paligot.confily.networking.routes.MyProfile
import com.paligot.confily.networking.ui.models.ExportNetworkingUi
import com.paligot.confily.style.theme.actions.FabAction
import com.paligot.confily.style.theme.actions.TabActionsUi
import com.paligot.confily.style.theme.actions.TopActionsUi
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class NetworkingUiState {
    data object Loading : NetworkingUiState()
    data class Success(
        val topActionsUi: TopActionsUi,
        val tabActionsUi: TabActionsUi,
        val fabAction: FabAction?
    ) : NetworkingUiState()

    data class Failure(val throwable: Throwable) : NetworkingUiState()
}

class NetworkingViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _exportPath = MutableSharedFlow<ExportNetworkingUi>(replay = 1)
    val exportPath: SharedFlow<ExportNetworkingUi> = _exportPath

    private val _innerRoute = MutableStateFlow<String?>(null)
    val uiState = combine(
        flow = _innerRoute,
        flow2 = userRepository.fetchConfiguration(),
        transform = { route, config ->
            NetworkingUiState.Success(
                topActionsUi = TopActionsUi(
                    actions = if (config.countUsersScanned > 0) {
                        persistentListOf(TopActions.export)
                    } else {
                        persistentListOf()
                    }
                ),
                tabActionsUi = TabActionsUi(
                    scrollable = true,
                    actions = if (config.hasProfileCompleted) {
                        persistentListOf(
                            TabActions.myProfile,
                            TabActions.contacts
                        )
                    } else {
                        persistentListOf(TabActions.myProfile)
                    }
                ),
                fabAction = when (route) {
                    MyProfile.navDeeplink() -> if (!config.hasProfileCompleted) FabActions.createProfile else null
                    Contacts.navDeeplink() -> FabActions.scanContact
                    else -> null
                }
            ) as NetworkingUiState
        }
    ).catch {
        emit(NetworkingUiState.Failure(it))
    }.stateIn(
        scope = viewModelScope,
        initialValue = NetworkingUiState.Loading,
        started = SharingStarted.WhileSubscribed()
    )

    fun innerScreenConfig(route: String) = viewModelScope.launch {
        _innerRoute.update { route }
    }

    fun exportNetworking() = viewModelScope.launch {
        _exportPath.tryEmit(userRepository.exportUserScanned().mapToExportNetworkingUi())
    }
}
