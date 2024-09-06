package com.paligot.confily.backend.sessions

import com.paligot.confily.backend.internals.helpers.database.Database
import com.paligot.confily.backend.internals.helpers.database.get
import com.paligot.confily.backend.internals.helpers.database.getAll
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

private object CollectionName {
    const val TALK_SESSIONS = "talks"
    const val EVENT_SESSIONS = "event-sessions"
}

private fun <T : SessionDb> T.getCollectionName() = when (this) {
    is TalkDb -> CollectionName.TALK_SESSIONS
    is EventSessionDb -> CollectionName.EVENT_SESSIONS
    else -> TODO("Session not implemented")
}

@Suppress("TooManyFunctions")
class SessionDao(
    private val database: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun get(eventId: String, id: String): SessionDb? =
        getTalkSession(eventId, id) ?: getEventSession(eventId, id)

    suspend fun getTalkSession(eventId: String, id: String): TalkDb? =
        database.get(eventId = eventId, collectionName = CollectionName.TALK_SESSIONS, id = id)

    suspend fun getEventSession(eventId: String, id: String): EventSessionDb? =
        database.get(eventId = eventId, collectionName = CollectionName.EVENT_SESSIONS, id = id)

    suspend fun getAll(eventId: String): List<SessionDb> = coroutineScope {
        val talkSessions = async(context = dispatcher) { getAllTalkSessions(eventId) }
        val eventSessions = async(context = dispatcher) { getAllEventSessions(eventId) }
        return@coroutineScope talkSessions.await() + eventSessions.await()
    }

    suspend fun getAllEventSessions(eventId: String): List<EventSessionDb> = database
        .getAll<EventSessionDb>(eventId = eventId, collectionName = CollectionName.EVENT_SESSIONS)

    suspend fun getAllTalkSessions(eventId: String): List<TalkDb> = database
        .getAll<TalkDb>(eventId = eventId, collectionName = CollectionName.TALK_SESSIONS)

    suspend fun <T : SessionDb> insert(eventId: String, session: T) {
        database.insert(
            eventId = eventId,
            collectionName = session.getCollectionName(),
            id = session.id,
            item = session
        )
    }

    suspend fun <T : SessionDb> insertAll(eventId: String, talks: List<T>) = coroutineScope {
        val asyncItems = talks.map {
            async(context = dispatcher) { insert(eventId, it) }
        }
        asyncItems.awaitAll()
        Unit
    }

    suspend fun <T : SessionDb> update(eventId: String, session: T) {
        database.update(
            eventId = eventId,
            collectionName = session.getCollectionName(),
            id = session.id,
            item = session
        )
    }

    suspend fun <T : SessionDb> createOrUpdate(eventId: String, session: T): String {
        if (session.id == "") {
            return database.insert(
                eventId = eventId,
                collectionName = session.getCollectionName(),
                transform = {
                    when (session) {
                        is TalkDb -> session.copy(it)
                        is EventSessionDb -> session.copy(it)
                        else -> TODO("Session not implemented")
                    }
                }
            )
        }
        val existing = get(eventId, session.id)
        if (existing == null) {
            insert(eventId, session)
        } else {
            update(eventId, session)
        }
        return session.id
    }

    suspend fun deleteDiffTalkSessions(eventId: String, ids: List<String>) {
        val diff = database.diff(eventId, CollectionName.TALK_SESSIONS, ids)
        database.deleteAll(eventId, CollectionName.TALK_SESSIONS, diff)
    }

    suspend fun deleteDiffEventSessions(eventId: String, ids: List<String>) {
        val diff = database.diff(eventId, CollectionName.EVENT_SESSIONS, ids)
        database.deleteAll(eventId, CollectionName.EVENT_SESSIONS, diff)
    }
}
