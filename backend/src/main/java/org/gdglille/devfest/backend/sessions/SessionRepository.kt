package org.gdglille.devfest.backend.sessions

import org.gdglille.devfest.backend.NotAcceptableException
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.third.parties.geocode.GeocodeApi
import org.gdglille.devfest.backend.third.parties.geocode.convertToDb
import org.gdglille.devfest.models.Session
import org.gdglille.devfest.models.inputs.EventSessionInput

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
