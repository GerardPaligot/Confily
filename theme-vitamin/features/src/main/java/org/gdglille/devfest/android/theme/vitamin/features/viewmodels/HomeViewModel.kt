package org.gdglille.devfest.android.theme.vitamin.features.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gdglille.devfest.android.theme.vitamin.ui.BottomActions
import org.gdglille.devfest.android.theme.vitamin.ui.FabActions
import org.gdglille.devfest.android.theme.vitamin.ui.Screen
import org.gdglille.devfest.android.theme.vitamin.ui.TabActions
import org.gdglille.devfest.android.theme.vitamin.ui.TopActions
import org.gdglille.devfest.android.ui.resources.actions.BottomAction
import org.gdglille.devfest.android.ui.resources.actions.FabAction
import org.gdglille.devfest.android.ui.resources.actions.TabAction
import org.gdglille.devfest.android.ui.resources.models.BottomActionsUi
import org.gdglille.devfest.android.ui.resources.models.ScreenUi
import org.gdglille.devfest.android.ui.resources.models.TabActionsUi
import org.gdglille.devfest.android.ui.resources.models.TopActionsUi
import org.gdglille.devfest.models.ExportNetworkingUi
import org.gdglille.devfest.models.ScaffoldConfigUi
import org.gdglille.devfest.models.UserNetworkingUi
import org.gdglille.devfest.repositories.AgendaRepository
import org.gdglille.devfest.repositories.EventRepository
import org.gdglille.devfest.repositories.UserRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val screenUi: ScreenUi) : HomeUiState()
    data class Failure(val throwable: Throwable) : HomeUiState()
}

class HomeViewModel(
    private val agendaRepository: AgendaRepository,
    private val userRepository: UserRepository,
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _configState = MutableStateFlow(ScaffoldConfigUi())
    private val _routeState = MutableStateFlow<String?>(null)
    private val _uiTopState = MutableStateFlow(TopActionsUi())
    private val _uiTabState = MutableStateFlow(TabActionsUi())
    private val _uiFabState = MutableStateFlow<FabAction?>(null)
    private val _uiBottomState = MutableStateFlow(BottomActionsUi())
    private val _uiIsFavState = MutableStateFlow(false)
    private val _exportPath = MutableSharedFlow<ExportNetworkingUi>(replay = 1)
    private val _uiState = combine(
        _routeState,
        _configState,
        _uiIsFavState,
        transform = { route, config, isFav ->
            if (route == null) return@combine HomeUiState.Loading
            updateUiTopActions(route, config, isFav)
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
    val uiTopState: StateFlow<TopActionsUi> = _uiTopState
    val uiTabState: StateFlow<TabActionsUi> = _uiTabState
    val uiFabState: StateFlow<FabAction?> = _uiFabState
    val uiBottomState: StateFlow<BottomActionsUi> = _uiBottomState
    val exportPath: SharedFlow<ExportNetworkingUi> = _exportPath
    val uiState: StateFlow<HomeUiState> = _uiState

    private fun updateUiTopActions(route: String, config: ScaffoldConfigUi, isFav: Boolean) {
        val topActions = when (route) {
            Screen.Agenda.route -> TopActionsUi(
                actions = persistentListOf(if (isFav) TopActions.favoriteFilled else TopActions.favorite)
            )
            Screen.Networking.route -> TopActionsUi(
                actions = if (config.hasUsersInNetworking) persistentListOf(TopActions.export) else persistentListOf()
            )
            Screen.Info.route -> TopActionsUi(
                actions = persistentListOf(TopActions.disconnect),
                maxActions = 0
            )
            else -> TopActionsUi()
        }
        if (topActions != _uiTopState.value) {
            _uiTopState.value = topActions
        }
    }

    private fun updateUiTabActions(route: String, config: ScaffoldConfigUi) {
        val tabActions = when (route) {
            Screen.Agenda.route -> TabActionsUi(
                actions = config.agendaTabs.map {
                    val label = DateTimeFormatter
                        .ofPattern("dd MMM")
                        .format(LocalDate.parse(it))
                    TabAction(route = it, 0, label)
                }.toImmutableList(),
                scrollable = true
            )

            Screen.Networking.route -> TabActionsUi(
                actions = if (config.hasProfile) persistentListOf(
                    TabActions.myProfile,
                    TabActions.contacts
                ) else persistentListOf()
            )

            Screen.Info.route -> TabActionsUi(
                actions = arrayListOf<TabAction>().apply {
                    add(TabActions.event)
                    if (config.hasMenus) {
                        add(TabActions.menus)
                    }
                    if (config.hasQAndA) {
                        add(TabActions.qanda)
                    }
                    add(TabActions.coc)
                }.toImmutableList(),
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
        val bottomActions = BottomActionsUi(
            actions = arrayListOf<BottomAction>().apply {
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
            }.toImmutableList()
        )
        if (bottomActions != _uiBottomState.value) {
            _uiBottomState.value = bottomActions
        }
    }

    init {
        viewModelScope.launch {
            arrayListOf(
                async {
                    try {
                        agendaRepository.fetchAndStoreAgenda()
                    } catch (_: Throwable) {
                    }
                },
                async {
                    try {
                        merge(
                            agendaRepository.scaffoldConfig(),
                            agendaRepository.isFavoriteToggled()
                        ).collect {
                            when (it) {
                                is ScaffoldConfigUi -> _configState.value = it
                                is Boolean -> _uiIsFavState.value = it
                                else -> TODO("Flow not implemented")
                            }
                        }
                    } catch (_: Throwable) {
                    }
                }
            ).awaitAll()
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

    fun toggleFavoriteFiltering() = viewModelScope.launch {
        agendaRepository.toggleFavoriteFiltering()
    }

    fun disconnect() = viewModelScope.launch {
        eventRepository.deleteEventId()
    }

    fun exportNetworking() = viewModelScope.launch {
        _exportPath.tryEmit(userRepository.exportNetworking())
    }

    object Factory {
        fun create(
            agendaRepository: AgendaRepository,
            userRepository: UserRepository,
            eventRepository: EventRepository
        ) =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T =
                    HomeViewModel(
                        agendaRepository = agendaRepository,
                        userRepository = userRepository,
                        eventRepository = eventRepository
                    ) as T
            }
    }
}
