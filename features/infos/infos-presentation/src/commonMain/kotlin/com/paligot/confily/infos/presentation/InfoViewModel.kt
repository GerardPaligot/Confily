package com.paligot.confily.infos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.infos.routes.Info
import com.paligot.confily.navigation.FabActions
import com.paligot.confily.navigation.TabActions
import com.paligot.confily.navigation.TopActions
import com.paligot.confily.style.theme.actions.FabAction
import com.paligot.confily.style.theme.actions.TabAction
import com.paligot.confily.style.theme.actions.TabActionsUi
import com.paligot.confily.style.theme.actions.TopActionsUi
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class InfoUiState {
    data object Loading : InfoUiState()
    data class Success(
        val topActionsUi: TopActionsUi,
        val tabActionsUi: TabActionsUi,
        val fabAction: FabAction?
    ) : InfoUiState()

    data class Failure(val throwable: Throwable) : InfoUiState()
}

class InfoViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _innerRoute = MutableStateFlow<String?>(null)
    val uiState = combine(
        flow = _innerRoute,
        flow2 = eventRepository.featureFlags(),
        transform = { route, features ->
            InfoUiState.Success(
                topActionsUi = TopActionsUi(
                    actions = persistentListOf(TopActions.disconnect),
                    maxActions = 0
                ),
                tabActionsUi = TabActionsUi(
                    scrollable = true,
                    actions = arrayListOf<TabAction>().apply {
                        add(TabActions.event)
                        add(TabActions.maps)
                        if (features.hasMenus) {
                            add(TabActions.menus)
                        }
                        if (features.hasQAndA) {
                            add(TabActions.qanda)
                        }
                        add(TabActions.coc)
                        if (features.hasTeamMembers) {
                            add(TabActions.teamMembers)
                        }
                    }.toImmutableList()
                ),
                fabAction = when (route) {
                    Info.navDeeplink() -> if (features.hasTicketIntegration) FabActions.scanTicket else null
                    else -> null
                }
            ) as InfoUiState
        }
    ).catch {
        emit(InfoUiState.Failure(it))
    }.stateIn(
        scope = viewModelScope,
        initialValue = InfoUiState.Loading,
        started = SharingStarted.WhileSubscribed()
    )

    fun innerScreenConfig(route: String) = viewModelScope.launch {
        _innerRoute.update { route }
    }

    fun disconnect() = viewModelScope.launch {
        eventRepository.deleteEventId()
    }
}
