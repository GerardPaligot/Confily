package com.paligot.confily.backend.sessions.application

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.EventSessionEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.SessionFirestore
import com.paligot.confily.backend.sessions.domain.SessionAdminRepository
import com.paligot.confily.backend.third.parties.geocode.GeocodeApi
import com.paligot.confily.backend.third.parties.geocode.convertToEntity
import com.paligot.confily.models.inputs.EventSessionInput
import com.paligot.confily.models.inputs.TalkSessionInput

class SessionAdminRepositoryDefault(
    private val eventDao: EventFirestore,
    private val sessionFirestore: SessionFirestore,
    private val geocodeApi: GeocodeApi
) : SessionAdminRepository {
    override suspend fun create(eventId: String, input: TalkSessionInput): String {
        val event = eventDao.get(eventId)
        val talkDb = input.convertToEntity()
        val id = sessionFirestore.createOrUpdate(eventId, talkDb)
        eventDao.updateAgendaUpdatedAt(event)
        return id
    }

    override suspend fun update(eventId: String, talkId: String, input: TalkSessionInput): String {
        val event = eventDao.get(eventId)
        sessionFirestore.createOrUpdate(eventId, input.convertToEntity(id = talkId))
        eventDao.updateAgendaUpdatedAt(event)
        return talkId
    }

    override suspend fun update(eventId: String, sessionId: String, input: EventSessionInput): String {
        val sessionDb = sessionFirestore.get(eventId, sessionId)
        if (sessionDb !is EventSessionEntity) {
            throw NotFoundException("Event Session $sessionId Not Found")
        }
        val addressDb = input.address?.let {
            geocodeApi.geocode(it).convertToEntity()
                ?: throw NotAcceptableException("Your address information isn't found")
        }
        return sessionFirestore.createOrUpdate(
            eventId,
            input.convertToEntity(session = sessionDb, address = addressDb)
        )
    }
}
