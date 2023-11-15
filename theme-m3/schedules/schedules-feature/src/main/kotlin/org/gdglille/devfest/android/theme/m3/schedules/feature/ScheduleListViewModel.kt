package org.gdglille.devfest.android.theme.m3.schedules.feature

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gdglille.devfest.AlarmScheduler
import org.gdglille.devfest.android.theme.m3.navigation.TopActions
import org.gdglille.devfest.android.theme.m3.style.actions.TabAction
import org.gdglille.devfest.android.theme.m3.style.actions.TabActionsUi
import org.gdglille.devfest.android.theme.m3.style.actions.TopAction
import org.gdglille.devfest.android.theme.m3.style.actions.TopActionsUi
import org.gdglille.devfest.models.ui.AgendaUi
import org.gdglille.devfest.models.ui.ScaffoldConfigUi
import org.gdglille.devfest.models.ui.TalkItemUi
import org.gdglille.devfest.repositories.AgendaRepository

data class ScheduleUi(
    val topActionsUi: TopActionsUi,
    val tabActionsUi: TabActionsUi,
    val schedules: ImmutableList<AgendaUi>
)

sealed class ScheduleListUiState {
    data class Loading(val agenda: ImmutableList<AgendaUi>) : ScheduleListUiState()
    data class Success(val scheduleUi: ScheduleUi) : ScheduleListUiState()
    data class Failure(val throwable: Throwable) : ScheduleListUiState()
}

@FlowPreview
@ExperimentalCoroutinesApi
class ScheduleListViewModel(
    private val repository: AgendaRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    private val _tabsStates = repository.scaffoldConfig()
        .map {
            TabActionsUi(
                scrollable = true,
                actions = it.agendaTabs.map {
                    val label = DateTimeFormatter.ofPattern("dd MMM").format(LocalDate.parse(it))
                    TabAction(route = it, 0, label)
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
    val uiState: StateFlow<ScheduleListUiState> =
        combine(
            _tabsStates,
            _uiHasFiltersState,
            _schedules,
            transform = { agendaTabs, topActions, schedules ->
                if (schedules.isNotEmpty()) {
                    ScheduleListUiState.Success(
                        ScheduleUi(
                            topActionsUi = topActions,
                            tabActionsUi = agendaTabs,
                            schedules = schedules.values.toImmutableList()
                        )
                    )
                } else {
                    ScheduleListUiState.Loading(persistentListOf(AgendaUi.fake))
                }
            }
        ).catch {
            Firebase.crashlytics.recordException(it)
            ScheduleListUiState.Failure(it)
        }.stateIn(
            scope = viewModelScope,
            initialValue = ScheduleListUiState.Loading(persistentListOf(AgendaUi.fake)),
            started = SharingStarted.WhileSubscribed()
        )

    @SuppressLint("UnspecifiedImmutableFlag")
    fun markAsFavorite(context: Context, talkItem: TalkItemUi) = viewModelScope.launch {
        alarmScheduler.schedule(context, talkItem)
    }

    object Factory {
        fun create(
            repository: AgendaRepository,
            alarmScheduler: AlarmScheduler
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T = ScheduleListViewModel(
                repository = repository,
                alarmScheduler = alarmScheduler
            ) as T
        }
    }
}
