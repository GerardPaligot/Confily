package org.gdglille.devfest.backend.speakers

import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.internals.CommonApi
import org.gdglille.devfest.models.inputs.SpeakerInput

class SpeakerRepository(
    private val commonApi: CommonApi,
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao
) {
    suspend fun list(eventId: String) = coroutineScope {
        return@coroutineScope speakerDao.getAll(eventId).map { it.convertToModel() }
    }

    suspend fun create(eventId: String, apiKey: String, speakerInput: SpeakerInput) =
        coroutineScope {
            val event = eventDao.getVerified(eventId, apiKey)
            val speakerDb = speakerInput.convertToDb(photoUrl = speakerInput.photoUrl)
            val id = speakerDao.createOrUpdate(eventId, speakerDb)
            val avatar = commonApi.fetchByteArray(speakerInput.photoUrl)
            val bucketItem = speakerDao.saveProfile(eventId = eventId, id = id, content = avatar)
            speakerDao.createOrUpdate(eventId, speakerDb.copy(photoUrl = bucketItem.url))
            eventDao.updateAgendaUpdatedAt(event)
            return@coroutineScope id
        }

    suspend fun get(eventId: String, speakerId: String) = coroutineScope {
        val speaker = speakerDao.get(eventId, speakerId)
            ?: throw NotFoundException("Speaker with $speakerId is not found")
        return@coroutineScope speaker.convertToModel()
    }

    suspend fun update(
        eventId: String,
        apiKey: String,
        speakerId: String,
        speakerInput: SpeakerInput
    ) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        val avatar = commonApi.fetchByteArray(speakerInput.photoUrl)
        val bucketItem = speakerDao.saveProfile(eventId = eventId, id = speakerId, content = avatar)
        val speakerDb = speakerInput.convertToDb(photoUrl = bucketItem.url, speakerId)
        eventDao.updateAgendaUpdatedAt(event)
        return@coroutineScope speakerDao.createOrUpdate(eventId, speakerDb)
    }
}
