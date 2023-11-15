package org.gdglille.devfest.android.theme.m3.networking.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gdglille.devfest.android.theme.m3.navigation.TabActions
import org.gdglille.devfest.android.theme.m3.navigation.TopActions
import org.gdglille.devfest.android.theme.m3.style.actions.TabActionsUi
import org.gdglille.devfest.android.theme.m3.style.actions.TopActionsUi
import org.gdglille.devfest.models.ui.ExportNetworkingUi
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.UserRepository

sealed class NetworkingUiState {
    data object Loading : NetworkingUiState()
    data class Success(val topActionsUi: TopActionsUi, val tabActionsUi: TabActionsUi) :
        NetworkingUiState()

    data class Failure(val throwable: Throwable) : NetworkingUiState()
}

class NetworkingViewModel(
    private val agendaRepository: AgendaRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _exportPath = MutableSharedFlow<ExportNetworkingUi>(replay = 1)
    val exportPath: SharedFlow<ExportNetworkingUi> = _exportPath
    val uiState = agendaRepository.scaffoldConfig()
        .map { config ->
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
                )
            )
        }
        .catch {
            Firebase.crashlytics.recordException(it)
            NetworkingUiState.Failure(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = NetworkingUiState.Loading,
            started = SharingStarted.WhileSubscribed()
        )

    fun exportNetworking() = viewModelScope.launch {
        _exportPath.tryEmit(userRepository.exportNetworking())
    }

    object Factory {
        fun create(agendaRepository: AgendaRepository, userRepository: UserRepository) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    NetworkingViewModel(
                        agendaRepository = agendaRepository,
                        userRepository = userRepository
                    ) as T
            }
    }
}
