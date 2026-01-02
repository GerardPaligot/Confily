package com.paligot.confily.backend.sessions.infrastructure.firestore

import com.google.cloud.firestore.Firestore
import com.paligot.confily.backend.internals.helpers.database.batchDelete
import com.paligot.confily.backend.internals.helpers.database.diffRefs
import com.paligot.confily.backend.internals.helpers.database.getDocument
import com.paligot.confily.backend.internals.helpers.database.getDocuments
import com.paligot.confily.backend.internals.helpers.database.insert
import com.paligot.confily.backend.internals.helpers.database.update
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

private object CollectionName {
    const val TALK_SESSIONS = "talks"
    const val EVENT_SESSIONS = "event-sessions"
}

private fun <T : SessionEntity> T.getCollectionName() = when (this) {
    is TalkSessionEntity -> CollectionName.TALK_SESSIONS
    is EventSessionEntity -> CollectionName.EVENT_SESSIONS
}

@Suppress("TooManyFunctions")
class SessionFirestore(
    private val projectName: String,
    private val firestore: Firestore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun get(eventId: String, id: String): SessionEntity? =
        getTalkSession(eventId, id) ?: getEventSession(eventId, id)

    fun getTalkSession(eventId: String, id: String): TalkSessionEntity? = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName.TALK_SESSIONS)
        .getDocument(id)

    fun getEventSession(eventId: String, id: String): EventSessionEntity? = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName.EVENT_SESSIONS)
        .getDocument(id)

    suspend fun getAll(eventId: String): List<SessionEntity> = coroutineScope {
        val talkSessions = async(context = dispatcher) { getAllTalkSessions(eventId) }
        val eventSessions = async(context = dispatcher) { getAllEventSessions(eventId) }
        return@coroutineScope talkSessions.await() + eventSessions.await()
    }

    private fun getAllEventSessions(eventId: String): List<EventSessionEntity> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName.EVENT_SESSIONS)
        .getDocuments()

    fun getAllTalkSessions(eventId: String): List<TalkSessionEntity> = firestore
        .collection(projectName)
        .document(eventId)
        .collection(CollectionName.TALK_SESSIONS)
        .getDocuments()

    fun <T : SessionEntity> insert(eventId: String, session: T) {
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(session.getCollectionName())
            .insert(session.id, session)
    }

    suspend fun <T : SessionEntity> insertAll(eventId: String, talks: List<T>) = coroutineScope {
        val asyncItems = talks.map {
            async(context = dispatcher) { insert(eventId, it) }
        }
        asyncItems.awaitAll()
        Unit
    }

    fun <T : SessionEntity> update(eventId: String, session: T) {
        firestore
            .collection(projectName)
            .document(eventId)
            .collection(session.getCollectionName())
            .update(session.id, session)
    }

    fun <T : SessionEntity> createOrUpdate(eventId: String, session: T): String {
        if (session.id == "") {
            return firestore
                .collection(projectName)
                .document(eventId)
                .collection(session.getCollectionName())
                .insert {
                    when (session) {
                        is TalkSessionEntity -> session.copy(it)
                        is EventSessionEntity -> session.copy(it)
                        else -> TODO("Session not implemented")
                    }
                }
        }
        val existing = get(eventId, session.id)
        if (existing == null) {
            insert(eventId, session)
        } else {
            update(eventId, session)
        }
        return session.id
    }

    fun deleteDiffTalkSessions(eventId: String, ids: List<String>) {
        val diff = firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName.TALK_SESSIONS)
            .diffRefs(ids)
        firestore.batchDelete(diff)
    }

    fun deleteDiffEventSessions(eventId: String, ids: List<String>) {
        val diff = firestore
            .collection(projectName)
            .document(eventId)
            .collection(CollectionName.EVENT_SESSIONS)
            .diffRefs(ids)
        firestore.batchDelete(diff)
    }
}
