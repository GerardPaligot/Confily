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
import org.gdglille.devfest.android.theme.vitamin.features.models.ScreenUi
import org.gdglille.devfest.android.theme.vitamin.ui.screens.BottomActions
import org.gdglille.devfest.android.theme.vitamin.ui.screens.Screen
import org.gdglille.devfest.android.theme.vitamin.ui.screens.TabActions
import org.gdglille.devfest.android.ui.resources.BottomAction
import org.gdglille.devfest.android.ui.resources.TabAction
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
    private val _configState = MutableStateFlow(
        ScaffoldConfigUi(
            hasNetworking = false,
            hasSpeakerList = false,
            hasPartnerList = false,
            hasMenus = false,
            hasQAndA = false
        )
    )
    private val _routeState = MutableStateFlow<String?>(null)
    private val _uiState = combine(
        _routeState,
        _configState,
        transform = { route, config ->
            if (route == null) return@combine HomeUiState.Loading
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
            val tabActions = if (BottomActions.info.selectedRoutes.contains(route)) {
                arrayListOf<TabAction>().apply {
                    add(TabActions.event)
                    if (config.hasMenus) {
                        add(TabActions.menus)
                    }
                    if (config.hasQAndA) {
                        add(TabActions.qanda)
                    }
                    add(TabActions.coc)
                }
            } else if (BottomActions.networking.selectedRoutes.contains(route)) {
                arrayListOf(TabActions.myProfile, TabActions.contacts)
            } else {
                emptyList()
            }
            return@combine when (route) {
                Screen.Agenda.route -> HomeUiState.Success(
                    ScreenUi(title = Screen.Agenda.title, bottomActions = bottomActions)
                )

                Screen.SpeakerList.route -> HomeUiState.Success(
                    ScreenUi(title = Screen.SpeakerList.title, bottomActions = bottomActions)
                )

                Screen.MyProfile.route -> HomeUiState.Success(
                    ScreenUi(title = Screen.MyProfile.title, tabActions = tabActions, bottomActions = bottomActions)
                )

                Screen.Contacts.route -> HomeUiState.Success(
                    ScreenUi(title = Screen.Contacts.title, tabActions = tabActions, bottomActions = bottomActions)
                )

                Screen.Partners.route -> HomeUiState.Success(
                    ScreenUi(title = Screen.Partners.title, bottomActions = bottomActions)
                )

                Screen.Event.route -> HomeUiState.Success(
                    ScreenUi(title = Screen.Event.title, tabActions = tabActions, bottomActions = bottomActions)
                )

                Screen.Menus.route -> HomeUiState.Success(
                    ScreenUi(title = Screen.Menus.title, tabActions = tabActions, bottomActions = bottomActions)
                )

                Screen.QAndA.route -> HomeUiState.Success(
                    ScreenUi(title = Screen.QAndA.title, tabActions = tabActions, bottomActions = bottomActions)
                )

                Screen.CoC.route -> HomeUiState.Success(
                    ScreenUi(title = Screen.CoC.title, tabActions = tabActions, bottomActions = bottomActions)
                )

                else -> TODO()
            }
        }
    ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initialValue = HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState

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
