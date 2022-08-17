package org.gdglille.devfest.android.theme.vitamin.features.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gdglille.devfest.android.theme.vitamin.ui.BottomActions
import org.gdglille.devfest.android.theme.vitamin.ui.FabActions
import org.gdglille.devfest.android.theme.vitamin.ui.Screen
import org.gdglille.devfest.android.theme.vitamin.ui.TabActions
import org.gdglille.devfest.android.ui.resources.actions.BottomAction
import org.gdglille.devfest.android.ui.resources.actions.FabAction
import org.gdglille.devfest.android.ui.resources.actions.TabAction
import org.gdglille.devfest.android.ui.resources.models.ScreenUi
import org.gdglille.devfest.android.ui.resources.models.TabActionsUi
import org.gdglille.devfest.models.ScaffoldConfigUi
import org.gdglille.devfest.models.UserNetworkingUi
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.UserRepository

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val screenUi: ScreenUi) : HomeUiState()
    data class Failure(val throwable: Throwable) : HomeUiState()
}

class HomeViewModel(
    private val agendaRepository: AgendaRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _configState = MutableStateFlow(ScaffoldConfigUi())
    private val _routeState = MutableStateFlow<String?>(null)
    private val _uiTabState = MutableStateFlow(TabActionsUi())
    private val _uiFabState = MutableStateFlow<FabAction?>(null)
    private val _uiBottomState = MutableStateFlow<List<BottomAction>>(emptyList())
    private val _uiState = combine(
        _routeState,
        _configState,
        transform = { route, config ->
            if (route == null) return@combine HomeUiState.Loading
            updateUiBottomActions(config)
            updateUiTabActions(route, config)
            updateUiFabAction(route, config)
            return@combine when (route) {
                Screen.Agenda.route -> HomeUiState.Success(ScreenUi(title = Screen.Agenda.title))
                Screen.SpeakerList.route -> HomeUiState.Success(ScreenUi(title = Screen.SpeakerList.title))
                Screen.Networking.route -> HomeUiState.Success(ScreenUi(title = Screen.Networking.title))
                Screen.Partners.route -> HomeUiState.Success(ScreenUi(title = Screen.Partners.title))
                Screen.Info.route -> HomeUiState.Success(ScreenUi(title = Screen.Info.title))
                else -> TODO()
            }
        }
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initialValue = HomeUiState.Loading)
    val uiTabState: StateFlow<TabActionsUi> = _uiTabState
    val uiFabState: StateFlow<FabAction?> = _uiFabState
    val uiBottomState: StateFlow<List<BottomAction>> = _uiBottomState
    val uiState: StateFlow<HomeUiState> = _uiState

    private fun updateUiTabActions(route: String, config: ScaffoldConfigUi) {
        val tabActions = when (route) {
            Screen.Networking.route -> TabActionsUi(
                tabActions = if (config.hasProfile) arrayListOf(
                    TabActions.myProfile,
                    TabActions.contacts
                ) else emptyList()
            )

            Screen.Info.route -> TabActionsUi(
                tabActions = arrayListOf<TabAction>().apply {
                    add(TabActions.event)
                    if (config.hasMenus) {
                        add(TabActions.menus)
                    }
                    if (config.hasQAndA) {
                        add(TabActions.qanda)
                    }
                    add(TabActions.coc)
                },
                scrollable = true
            )

            else -> TabActionsUi()
        }
        if (tabActions != _uiTabState.value) {
            _uiTabState.value = tabActions
        }
    }

    private fun updateUiFabAction(route: String, config: ScaffoldConfigUi) {
        val fabAction = when (route) {
            Screen.MyProfile.route -> if (!config.hasProfile) FabActions.createProfile else null
            Screen.Contacts.route -> FabActions.scanContact
            Screen.Event.route -> if (config.hasBilletWebTicket) FabActions.scanTicket else null
            else -> null
        }
        if (fabAction != _uiFabState.value) {
            _uiFabState.value = fabAction
        }
    }

    private fun updateUiBottomActions(config: ScaffoldConfigUi) {
        val bottomActions = arrayListOf<BottomAction>().apply {
            add(BottomActions.agenda)
            if (config.hasSpeakerList) {
                add(BottomActions.speakers)
            }
            if (config.hasNetworking) {
                add(BottomActions.networking)
            }
            if (config.hasPartnerList) {
                add(BottomActions.partners)
            }
            add(BottomActions.info)
        }
        if (bottomActions != _uiBottomState.value) {
            _uiBottomState.value = bottomActions
        }
    }

    init {
        viewModelScope.launch {
            try {
                agendaRepository.scaffoldConfig().collect {
                    _configState.value = it
                }
            } catch (_: Throwable) {
            }
        }
    }

    fun screenConfig(route: String) = viewModelScope.launch {
        _routeState.value = route
    }

    fun updateFabUi(innerScreen: String) = viewModelScope.launch {
        updateUiFabAction(innerScreen, _configState.value)
    }

    fun saveTicket(barcode: String) = viewModelScope.launch {
        agendaRepository.insertOrUpdateTicket(barcode)
    }

    fun saveNetworkingProfile(user: UserNetworkingUi) = viewModelScope.launch {
        userRepository.insertNetworkingProfile(user)
    }

    object Factory {
        fun create(agendaRepository: AgendaRepository, userRepository: UserRepository) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    HomeViewModel(
                        agendaRepository = agendaRepository,
                        userRepository = userRepository
                    ) as T
            }
    }
}
