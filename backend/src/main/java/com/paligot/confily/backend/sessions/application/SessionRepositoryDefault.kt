package com.paligot.confily.backend.sessions.application

import com.paligot.confily.backend.events.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.sessions.domain.SessionRepository
import com.paligot.confily.backend.sessions.infrastructure.firestore.SessionFirestore
import com.paligot.confily.backend.sessions.infrastructure.firestore.convertToModel
import com.paligot.confily.models.Session

class SessionRepositoryDefault(
    private val eventDao: EventFirestore,
    private val sessionFirestore: SessionFirestore
) : SessionRepository {
    override suspend fun list(eventId: String): List<Session> {
        val eventDb = eventDao.get(eventId)
        return sessionFirestore.getAll(eventId).map { it.convertToModel(eventDb) }
    }
}
