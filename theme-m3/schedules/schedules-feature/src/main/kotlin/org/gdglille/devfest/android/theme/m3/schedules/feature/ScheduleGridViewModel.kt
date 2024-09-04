package org.gdglille.devfest.android.theme.m3.schedules.feature

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.paligot.confily.core.AlarmScheduler
import com.paligot.confily.core.repositories.AgendaRepository
import com.paligot.confily.models.ui.TalkItemUi
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
import org.gdglille.devfest.theme.m3.navigation.TopActions
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.StringResource
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ScheduleUi(
    val topActionsUi: TopActionsUi,
    val tabActionsUi: TabActionsUi,
    val schedules: ImmutableList<com.paligot.confily.models.ui.AgendaUi>
)

sealed class ScheduleGridUiState {
    data class Loading(val agenda: ImmutableList<com.paligot.confily.models.ui.AgendaUi>) : ScheduleGridUiState()
    data class Success(val scheduleUi: ScheduleUi) : ScheduleGridUiState()
    data class Failure(val throwable: Throwable) : ScheduleGridUiState()
}

@OptIn(InternalResourceApi::class, ExperimentalResourceApi::class)
@FlowPreview
@ExperimentalCoroutinesApi
class ScheduleGridViewModel(
    repository: AgendaRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    private val _tabsStates = repository.scaffoldConfig()
        .map {
            TabActionsUi(
                scrollable = true,
                actions = it.agendaTabs.sorted().map {
                    val label = DateTimeFormatter.ofPattern("dd MMM").format(LocalDate.parse(it))
                    TabAction(
                        route = it,
                        StringResource("", "", emptySet()),
                        label
                    )
                }.toImmutableList()
            )
        }
    private val _uiHasFiltersState = repository.hasFilterApplied()
        .map {
            TopActionsUi(
                actions = persistentListOf(if (it) TopActions.filtersFilled else TopActions.filters)
            )
        }
    private val _schedules = repository.agenda()
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
                    ScheduleGridUiState.Loading(persistentListOf(com.paligot.confily.models.ui.AgendaUi.fake))
                }
            }
        ).catch {
            Firebase.crashlytics.recordException(it)
            ScheduleGridUiState.Failure(it)
        }.stateIn(
            scope = viewModelScope,
            initialValue = ScheduleGridUiState.Loading(persistentListOf(com.paligot.confily.models.ui.AgendaUi.fake)),
            started = SharingStarted.WhileSubscribed()
        )

    @SuppressLint("UnspecifiedImmutableFlag")
    fun markAsFavorite(context: Context, talkItem: TalkItemUi) = viewModelScope.launch {
        alarmScheduler.schedule(context, talkItem)
    }
}
