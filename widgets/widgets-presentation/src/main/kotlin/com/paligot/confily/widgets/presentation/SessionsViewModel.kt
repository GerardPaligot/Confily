package com.paligot.confily.widgets.presentation

import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.core.schedules.entities.SessionItem
import com.paligot.confily.resources.Strings
import com.paligot.confily.widgets.ui.models.SessionItemUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant

sealed class SessionsUiState {
    data object Loading : SessionsUiState()
    data class Success(val eventName: String?, val sessions: ImmutableList<SessionItemUi>) :
        SessionsUiState()
}

class SessionsViewModel(
    sessionRepository: SessionRepository,
    eventRepository: EventRepository,
    date: String,
    lyricist: Lyricist<Strings>,
    coroutineScope: CoroutineScope = CoroutineScope(Job())
) {
    val uiState: StateFlow<SessionsUiState> = combine(
        flow = eventRepository.event().catch { emit(null) },
        flow2 = sessionRepository.fetchNextTalks(date).catch { emit(persistentListOf()) },
        transform = { event, sessions ->
            if (sessions.isEmpty()) {
                SessionsUiState.Loading
            } else {
                SessionsUiState.Success(
                    event?.name,
                    sessions.map { it.mapToUi(lyricist.strings) }.toImmutableList()
                )
            }
        }
    ).stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SessionsUiState.Loading
    )
}

private fun SessionItem.mapToUi(strings: Strings): SessionItemUi {
    val diff = endTime.toInstant(TimeZone.currentSystemDefault())
        .minus(startTime.toInstant(TimeZone.currentSystemDefault()))
    return SessionItemUi(
        id = id,
        title = title,
        slotTime = startTime.format(
            LocalDateTime.Format {
                hour()
                char(':')
                minute()
            }
        ),
        room = room,
        duration = strings.texts.scheduleMinutes(diff.inWholeMinutes.toInt())
    )
}
