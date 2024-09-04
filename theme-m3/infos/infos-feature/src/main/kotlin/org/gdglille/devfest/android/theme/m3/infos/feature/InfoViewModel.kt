package org.gdglille.devfest.android.theme.m3.infos.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.paligot.confily.core.repositories.AgendaRepository
import com.paligot.confily.core.repositories.EventRepository
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
import org.gdglille.devfest.theme.m3.navigation.FabActions
import org.gdglille.devfest.theme.m3.navigation.Screen
import org.gdglille.devfest.theme.m3.navigation.TabActions
import org.gdglille.devfest.theme.m3.navigation.TopActions

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
    private val agendaRepository: AgendaRepository,
    private val eventRepository: EventRepository
) : ViewModel() {
    private val _innerRoute = MutableStateFlow<String?>(null)
    val uiState = combine(
        _innerRoute,
        agendaRepository.scaffoldConfig(),
        transform = { route, config ->
            InfoUiState.Success(
                topActionsUi = TopActionsUi(
                    actions = persistentListOf(TopActions.disconnect),
                    maxActions = 0
                ),
                tabActionsUi = TabActionsUi(
                    scrollable = true,
                    actions = arrayListOf<TabAction>().apply {
                        add(TabActions.event)
                        if (config.hasMenus) {
                            add(TabActions.menus)
                        }
                        if (config.hasQAndA) {
                            add(TabActions.qanda)
                        }
                        add(TabActions.coc)
                    }.toImmutableList()
                ),
                fabAction = when (route) {
                    Screen.Event.route -> if (config.hasBilletWebTicket) FabActions.scanTicket else null
                    else -> null
                }
            )
        }
    ).catch {
        Firebase.crashlytics.recordException(it)
        InfoUiState.Failure(it)
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
