package com.paligot.confily.widgets.presentation

import com.paligot.confily.core.repositories.EventRepository
import com.paligot.confily.core.schedules.SchedulesRepository
import com.paligot.confily.models.ui.EventInfoUi
import com.paligot.confily.models.ui.TalkItemUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

sealed class SessionsUiState {
    data object Loading : SessionsUiState()
    data class Success(val event: EventInfoUi?, val sessions: ImmutableList<TalkItemUi>) :
        SessionsUiState()
}

class SessionsViewModel(
    schedulesRepository: SchedulesRepository,
    eventRepository: EventRepository,
    date: String,
    coroutineScope: CoroutineScope = CoroutineScope(Job())
) {
    val uiState: StateFlow<SessionsUiState> = combine(
        flow = eventRepository.currentEvent()
            .catch { emit(null) },
        flow2 = schedulesRepository.fetchNextTalks(date)
            .catch { emit(persistentListOf()) },
        transform = { event, sessions ->
            if (sessions.isEmpty()) {
                SessionsUiState.Loading
            } else {
                SessionsUiState.Success(event, sessions)
            }
        }
    ).stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SessionsUiState.Loading
    )
}
