package com.paligot.confily.schedules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.AlarmScheduler
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.events.entities.mapToDays
import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.core.schedules.entities.mapToListUi
import com.paligot.confily.models.ui.AgendaUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.navigation.TopActions
import com.paligot.confily.resources.Strings
import com.paligot.confily.style.theme.actions.TabAction
import com.paligot.confily.style.theme.actions.TabActionsUi
import com.paligot.confily.style.theme.actions.TopActionsUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.StringResource

data class ScheduleUi(
    val topActionsUi: TopActionsUi,
    val tabActionsUi: TabActionsUi,
    val schedules: ImmutableList<AgendaUi>
)

sealed class ScheduleGridUiState {
    data class Loading(val agenda: ImmutableList<AgendaUi>) : ScheduleGridUiState()
    data class Success(val scheduleUi: ScheduleUi) : ScheduleGridUiState()
    data class Failure(val throwable: Throwable) : ScheduleGridUiState()
}

@OptIn(InternalResourceApi::class)
@FlowPreview
@ExperimentalCoroutinesApi
class ScheduleGridViewModel(
    eventRepository: EventRepository,
    sessionRepository: SessionRepository,
    lyricist: Lyricist<Strings>,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    private val _tabsStates = eventRepository.event().map { event ->
        if (event == null) return@map TabActionsUi()
        return@map TabActionsUi(
            scrollable = true,
            actions = event.mapToDays().map {
                TabAction(
                    route = it,
                    labelId = StringResource("", "", emptySet()),
                    label = it
                )
            }.toImmutableList()
        )
    }
    private val _uiHasFiltersState = sessionRepository.countFilters().map {
        TopActionsUi(
            actions = persistentListOf(if (it > 0) TopActions.filtersFilled else TopActions.filters)
        )
    }
    private val _schedules = sessionRepository.sessions()
    val uiState: StateFlow<ScheduleGridUiState> =
        combine(
            flow = _tabsStates,
            flow2 = _uiHasFiltersState,
            flow3 = _schedules,
            transform = { agendaTabs, topActions, schedules ->
                if (schedules.sessions.isNotEmpty()) {
                    ScheduleGridUiState.Success(
                        ScheduleUi(
                            topActionsUi = topActions,
                            tabActionsUi = agendaTabs,
                            schedules = schedules.mapToListUi(lyricist.strings)
                        )
                    )
                } else {
                    ScheduleGridUiState.Loading(persistentListOf(AgendaUi.fake))
                }
            }
        ).catch {
            it.printStackTrace()
            emit(ScheduleGridUiState.Failure(it))
        }.stateIn(
            scope = viewModelScope,
            initialValue = ScheduleGridUiState.Loading(persistentListOf(AgendaUi.fake)),
            started = SharingStarted.WhileSubscribed()
        )

    fun markAsFavorite(talkItem: TalkItemUi) = viewModelScope.launch {
        alarmScheduler.schedule(talkItem)
    }
}
