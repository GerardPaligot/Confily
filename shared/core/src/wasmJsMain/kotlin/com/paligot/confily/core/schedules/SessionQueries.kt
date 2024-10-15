package com.paligot.confily.core.schedules

import com.paligot.confily.core.combineAllSerializableScopedFlow
import com.paligot.confily.core.getAllSerializableScoped
import com.paligot.confily.core.getScopes
import com.paligot.confily.core.getSerializableScopedFlow
import com.paligot.confily.core.getSerializableScopedOrNull
import com.paligot.confily.core.putSerializableScoped
import com.paligot.confily.core.removeScoped
import com.paligot.confily.core.schedules.SessionQueries.Scopes.EVENTSESSIONS
import com.paligot.confily.core.schedules.SessionQueries.Scopes.SESSIONS
import com.paligot.confily.core.schedules.SessionQueries.Scopes.SESSIONWITHSPEAKERS
import com.paligot.confily.core.schedules.SessionQueries.Scopes.TALKSESSIONS
import com.paligot.confily.core.speakers.SpeakerDb
import com.paligot.confily.core.speakers.SpeakerQueries
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

class SessionQueries(
    private val settings: ObservableSettings,
    private val categoryQueries: CategoryQueries,
    private val formatQueries: FormatQueries,
    private val speakerQueries: SpeakerQueries
) {
    private object Scopes {
        const val SESSIONS = "sessions"
        const val TALKSESSIONS = "talksessions"
        const val EVENTSESSIONS = "eventsessions"
        const val SESSIONWITHSPEAKERS = "talksessionwithspeakers"
    }

    fun selectSessions(eventId: String): Flow<List<SelectSessionsDb>> = combine(
        flow = settings.combineAllSerializableScopedFlow<SessionDb>(
            scope = SESSIONS,
            filter = { it.eventId == eventId }
        ),
        flow2 = settings.combineAllSerializableScopedFlow<TalkSessionDb>(
            scope = TALKSESSIONS,
            filter = { it.eventId == eventId }
        ),
        flow3 = categoryQueries.selectCategories(eventId),
        flow4 = formatQueries.selectFormats(eventId),
        transform = { sessions, talks, categories, formats ->
            val talkIds = talks.map { it.id }
            sessions
                .filter { it.sessionTalkId in talkIds }
                .map { session ->
                    val talk = talks.find { it.id == session.sessionTalkId }!!
                    val category = categories.find { it.id == talk.categoryId }!!
                    val format = formats.find { it.id == talk.formatId }!!
                    SelectSessionsDb(session, talk, category, format)
                }
        }
    )

    fun selectBreakSessions(eventId: String): Flow<List<SelectEventSessionsDb>> = combine(
        flow = settings.combineAllSerializableScopedFlow<SessionDb>(
            scope = SESSIONS,
            filter = { it.eventId == eventId }
        ),
        flow2 = settings.combineAllSerializableScopedFlow<EventSessionDb>(
            scope = EVENTSESSIONS,
            filter = { it.eventId == eventId }
        ),
        transform = { sessions, events ->
            val eventIds = events.map { it.id }
            sessions
                .filter { it.sessionEventId in eventIds }
                .map { session ->
                    val event = events.find { it.id == session.sessionEventId }!!
                    SelectEventSessionsDb(session, event)
                }
        }
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    fun selectSessionByTalkId(eventId: String, talkId: String): Flow<SelectSessionsDb> {
        return settings.getSerializableScopedFlow<TalkSessionDb>(TALKSESSIONS, talkId)
            .flatMapConcat { talk ->
                settings.combineAllSerializableScopedFlow<SessionDb>(
                    scope = SESSIONS,
                    filter = { it.eventId == eventId }
                ).map { sessions ->
                    val session = sessions.find { it.id == talkId }!!
                    val category = categoryQueries.getCategory(talk.categoryId)!!
                    val format = formatQueries.getFormat(talk.formatId)!!
                    SelectSessionsDb(session, talk, category, format)
                }
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getSessionByTalkId(eventId: String, talkId: String): SelectSessionsDb {
        val talk = settings.getSerializableScopedOrNull<TalkSessionDb>(TALKSESSIONS, talkId)!!
        val sessions = settings.getAllSerializableScoped<SessionDb>(SESSIONS)
            .filter { it.eventId == eventId }
        val session = sessions.find { it.id == talkId }!!
        val category = categoryQueries.getCategory(talk.categoryId)!!
        val format = formatQueries.getFormat(talk.formatId)!!
        return SelectSessionsDb(session, talk, category, format)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun selectEventSessionById(eventId: String, sessionId: String): Flow<SelectEventSessionsDb> {
        return settings.getSerializableScopedFlow<EventSessionDb>(EVENTSESSIONS, sessionId)
            .flatMapConcat { event ->
                settings.combineAllSerializableScopedFlow<SessionDb>(
                    scope = SESSIONS,
                    filter = { it.eventId == eventId }
                ).map { sessions ->
                    val session = sessions.find { it.id == sessionId }!!
                    SelectEventSessionsDb(session, event)
                }
            }
    }

    fun getSpeakersByTalkId(eventId: String, talkId: String): List<SpeakerDb> {
        val talks = settings.getAllSerializableScoped<TalkSessionWithSpeakers>(SESSIONWITHSPEAKERS)
            .filter { it.eventId == eventId && it.talkId == talkId }
        val speakers = speakerQueries.getAllSpeakers()
        return talks.map { talk ->
            speakers.find { it.id == talk.speakerId }!!
        }
    }

    fun selectTalksBySpeakerId(
        eventId: String,
        speakerId: String
    ): Flow<List<SelectTalksBySpeakerIdDb>> = combine(
        flow = settings.combineAllSerializableScopedFlow<TalkSessionWithSpeakers>(
            scope = SESSIONWITHSPEAKERS,
            filter = { it.eventId == eventId && it.speakerId == speakerId }
        ),
        flow2 = settings.combineAllSerializableScopedFlow<TalkSessionDb>(
            scope = TALKSESSIONS,
            filter = { it.eventId == eventId }
        ),
        flow3 = categoryQueries.selectCategories(eventId),
        flow4 = formatQueries.selectFormats(eventId),
        transform = { speakers, sessions, categories, formats ->
            speakers.map { speaker ->
                val talk = sessions.find { it.id == speaker.talkId }!!
                val category = categories.find { it.id == talk.categoryId }!!
                val format = formats.find { it.id == talk.formatId }!!
                SelectTalksBySpeakerIdDb(talk, category, format)
            }
        }
    )

    fun selectDays(eventId: String): Flow<List<String>> =
        settings.combineAllSerializableScopedFlow<SessionDb>(SESSIONS) { it.eventId == eventId }
            .map { sessions -> sessions.map { it.date }.distinct() }

    fun countSessionsByFavorite(eventId: String, isFavorite: Boolean): Int =
        settings.getScopes(SESSIONS)
            .mapNotNull { getSessionOrNull(it) }
            .count { it.eventId == eventId && it.isFavorite == isFavorite }

    fun markAsFavorite(eventId: String, sessionId: String, isFavorite: Boolean) {
        val session = getSessionOrNull(sessionId) ?: return
        upsertSession(session.copy(isFavorite = isFavorite))
    }

    fun upsertSession(session: SessionDb) {
        settings.putSerializableScoped(SESSIONS, session.id, session)
    }

    fun deleteSessions(ids: List<String>) {
        ids.forEach {
            settings.removeScoped(SESSIONS, it)
        }
    }

    fun diffSessions(eventId: String, ids: List<String>): List<String> =
        settings.getScopes(SESSIONS).filter { eventId == eventId && it !in ids }

    fun upsertTalkSession(talk: TalkSessionDb) {
        settings.putSerializableScoped(TALKSESSIONS, talk.id, talk)
    }

    fun deleteTalkSessions(ids: List<String>) {
        ids.forEach {
            settings.removeScoped(TALKSESSIONS, it)
        }
    }

    fun diffTalkSessions(eventId: String, ids: List<String>): List<String> =
        settings.getScopes(TALKSESSIONS).filter { eventId == eventId && it !in ids }

    fun upsertEventSession(event: EventSessionDb) {
        settings.putSerializableScoped(EVENTSESSIONS, event.id, event)
    }

    fun upsertTalkWithSpeakers(talk: TalkSessionWithSpeakers) {
        settings.putSerializableScoped(SESSIONWITHSPEAKERS, talk.id, talk)
    }

    fun diffTalkWithSpeakers(eventId: String, ids: List<String>): List<String> =
        settings.getScopes(SESSIONWITHSPEAKERS).filter { eventId == eventId && it !in ids }

    fun deleteTalkWithSpeakers(ids: List<String>) {
        ids.forEach { settings.removeScoped(SESSIONWITHSPEAKERS, it) }
    }

    private fun getSessionOrNull(sessionId: String): SessionDb? =
        settings.getSerializableScopedOrNull(SESSIONS, sessionId)
}
