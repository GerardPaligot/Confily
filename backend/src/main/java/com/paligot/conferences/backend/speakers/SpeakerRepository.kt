package com.paligot.conferences.backend.speakers

import com.paligot.conferences.backend.NotFoundException
import com.paligot.conferences.backend.events.EventDao
import com.paligot.conferences.models.inputs.SpeakerInput
import kotlinx.coroutines.coroutineScope

class SpeakerRepository(
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao
) {
    suspend fun list(eventId: String) = coroutineScope {
        return@coroutineScope speakerDao.getAll(eventId).map { it.convertToModel() }
    }

    suspend fun create(eventId: String, apiKey: String, speakerInput: SpeakerInput) = coroutineScope {
        eventDao.getVerified(eventId, apiKey)
        val speakerDb = speakerInput.convertToDb()
        val id = speakerDao.createOrUpdate(eventId, speakerDb)
        eventDao.updateUpdatedAt(eventId)
        return@coroutineScope id
    }

    suspend fun get(eventId: String, speakerId: String) = coroutineScope {
        val speaker = speakerDao.get(eventId, speakerId)
            ?: throw NotFoundException("Speaker with $speakerId is not found")
        return@coroutineScope speaker.convertToModel()
    }

    suspend fun update(eventId: String, apiKey: String, speakerId: String, speakerInput: SpeakerInput) = coroutineScope {
        eventDao.getVerified(eventId, apiKey)
        val speakerDb = speakerInput.convertToDb(speakerId)
        eventDao.updateUpdatedAt(eventId)
        return@coroutineScope speakerDao.createOrUpdate(eventId, speakerDb)
    }
}