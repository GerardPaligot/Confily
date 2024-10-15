package com.paligot.confily.schedules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.AlarmScheduler
import com.paligot.confily.core.agenda.AgendaRepository
import com.paligot.confily.core.schedules.SchedulesRepository
import com.paligot.confily.models.ui.AgendaUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.navigation.TopActions
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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.byUnicodePattern
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
    agendaRepository: AgendaRepository,
    schedulesRepository: SchedulesRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    private val _tabsStates = agendaRepository.scaffoldConfig()
        .map {
            TabActionsUi(
                scrollable = true,
                actions = it.agendaTabs.sorted().map {
                    val label = LocalDate.parse(it).format(
                        LocalDate.Format {
                            byUnicodePattern("dd/MM")
                        }
                    )
                    TabAction(
                        route = it,
                        StringResource("", "", emptySet()),
                        label
                    )
                }.toImmutableList()
            )
        }
    private val _uiHasFiltersState = schedulesRepository.hasFilterApplied()
        .map {
            TopActionsUi(
                actions = persistentListOf(if (it) TopActions.filtersFilled else TopActions.filters)
            )
        }
    private val _schedules = schedulesRepository.agenda()
    val uiState: StateFlow<ScheduleGridUiState> =
        combine(
            _tabsStates,
            _uiHasFiltersState,
            _schedules,
            transform = { agendaTabs, topActions, schedules ->
                if (schedules.isNotEmpty()) {
                    ScheduleGridUiState.Success(
                        ScheduleUi(
                            topActionsUi = topActions,
                            tabActionsUi = agendaTabs,
                            schedules = schedules.values.toImmutableList()
                        )
                    )
                } else {
                    ScheduleGridUiState.Loading(persistentListOf(AgendaUi.fake))
                }
            }
        ).catch {
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
