package com.paligot.confily.core.sessions

import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.core.schedules.entities.mapToEventSessionUi
import com.paligot.confily.core.schedules.entities.mapToFiltersUi
import com.paligot.confily.core.schedules.entities.mapToMapUi
import com.paligot.confily.core.schedules.entities.mapToSessionUi
import com.paligot.confily.resources.Strings
import com.paligot.confily.schedules.panes.models.AgendaUi
import com.paligot.confily.schedules.panes.models.SessionUi
import com.paligot.confily.schedules.ui.models.EventSessionUi
import com.paligot.confily.schedules.ui.models.FiltersUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionInteractor(
    private val repository: SessionRepository,
    private val lyricist: Lyricist<Strings>
) {
    @NativeCoroutines
    fun sessions(): Flow<ImmutableMap<String, AgendaUi>> = repository.sessions()
        .map { it.mapToMapUi(lyricist.strings) }

    @NativeCoroutines
    fun session(sessionId: String): Flow<SessionUi> = repository.session(sessionId)
        .map { it.mapToSessionUi(lyricist.strings) }

    @NativeCoroutines
    fun eventSession(sessionId: String): Flow<EventSessionUi> = repository.eventSession(sessionId)
        .map { it.mapToEventSessionUi() }

    @NativeCoroutines
    fun filters(): Flow<FiltersUi> = repository.filters().map { it.mapToFiltersUi() }
}
