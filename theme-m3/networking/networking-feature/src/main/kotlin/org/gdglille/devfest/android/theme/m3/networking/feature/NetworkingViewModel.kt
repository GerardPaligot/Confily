package org.gdglille.devfest.android.theme.m3.networking.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
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
import org.gdglille.devfest.android.theme.m3.navigation.FabActions
import org.gdglille.devfest.android.theme.m3.navigation.Screen
import org.gdglille.devfest.android.theme.m3.navigation.TabActions
import org.gdglille.devfest.android.theme.m3.navigation.TopActions
import org.gdglille.devfest.android.theme.m3.style.actions.FabAction
import org.gdglille.devfest.android.theme.m3.style.actions.TabActionsUi
import org.gdglille.devfest.android.theme.m3.style.actions.TopActionsUi
import org.gdglille.devfest.models.ui.ExportNetworkingUi
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.UserRepository

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
    private val agendaRepository: AgendaRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _exportPath = MutableSharedFlow<ExportNetworkingUi>(replay = 1)
    val exportPath: SharedFlow<ExportNetworkingUi> = _exportPath

    private val _innerRoute = MutableStateFlow<String?>(null)
    val uiState = combine(
        _innerRoute,
        agendaRepository.scaffoldConfig(),
        transform = { route, config ->
            NetworkingUiState.Success(
                topActionsUi = TopActionsUi(
                    actions = if (config.hasUsersInNetworking) persistentListOf(TopActions.export)
                    else persistentListOf()
                ),
                tabActionsUi = TabActionsUi(
                    scrollable = true,
                    actions = if (config.hasProfile) persistentListOf(
                        TabActions.myProfile,
                        TabActions.contacts
                    )
                    else persistentListOf(TabActions.myProfile)
                ),
                fabAction = when (route) {
                    Screen.MyProfile.route -> if (!config.hasProfile) FabActions.createProfile else null
                    Screen.Contacts.route -> FabActions.scanContact
                    else -> null
                }
            )
        }
    ).catch {
        Firebase.crashlytics.recordException(it)
        NetworkingUiState.Failure(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = NetworkingUiState.Loading,
        started = SharingStarted.WhileSubscribed()
    )

    fun innerScreenConfig(route: String) = viewModelScope.launch {
        _innerRoute.update { route }
    }

    fun exportNetworking() = viewModelScope.launch {
        _exportPath.tryEmit(userRepository.exportNetworking())
    }
}
