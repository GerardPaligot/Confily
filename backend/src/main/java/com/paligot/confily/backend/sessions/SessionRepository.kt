package com.paligot.confily.backend.sessions

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.third.parties.geocode.GeocodeApi
import com.paligot.confily.backend.third.parties.geocode.convertToDb
import com.paligot.confily.models.Session
import com.paligot.confily.models.inputs.EventSessionInput

class SessionRepository(
    private val geocodeApi: GeocodeApi,
    private val eventDao: EventDao,
    private val sessionDao: SessionDao
) {
    suspend fun list(eventId: String): List<Session> {
        val eventDb = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        return sessionDao.getAll(eventId).map { it.convertToModel(eventDb) }
    }

    suspend fun update(
        eventId: String,
        apiKey: String,
        sessionId: String,
        input: EventSessionInput
    ): String {
        eventDao.getVerified(eventId, apiKey)
        val sessionDb = sessionDao.get(eventId, sessionId)
        if (sessionDb !is EventSessionDb) {
            throw NotFoundException("Event Session $sessionId Not Found")
        }
        val addressDb = input.address?.let {
            geocodeApi.geocode(it).convertToDb()
                ?: throw NotAcceptableException("Your address information isn't found")
        }
        return sessionDao.createOrUpdate(
            eventId,
            input.convertToDb(session = sessionDb, address = addressDb)
        )
    }
}
