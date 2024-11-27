package com.paligot.confily.core.sessions

import cafe.adriel.lyricist.Lyricist
import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.core.schedules.entities.mapToMapUi
import com.paligot.confily.core.schedules.entities.mapToUi
import com.paligot.confily.models.ui.AgendaUi
import com.paligot.confily.models.ui.EventSessionUi
import com.paligot.confily.models.ui.FiltersUi
import com.paligot.confily.models.ui.TalkUi
import com.paligot.confily.resources.Strings
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
    fun session(sessionId: String): Flow<TalkUi> = repository.session(sessionId)
        .map { it.mapToUi(lyricist.strings) }

    @NativeCoroutines
    fun eventSession(sessionId: String): Flow<EventSessionUi> = repository.eventSession(sessionId)
        .map { it.mapToUi() }

    @NativeCoroutines
    fun filters(): Flow<FiltersUi> = repository.filters().map { it.mapToUi() }
}
