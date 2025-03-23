package com.paligot.confily.backend.speakers

import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.internals.CommonApi
import com.paligot.confily.backend.internals.mimeType
import com.paligot.confily.models.inputs.SpeakerInput
import kotlinx.coroutines.coroutineScope

class SpeakerRepository(
    private val commonApi: CommonApi,
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao
) {
    suspend fun list(eventId: String) = coroutineScope {
        return@coroutineScope speakerDao.getAll(eventId).map { it.convertToModel() }
    }

    suspend fun create(eventId: String, speakerInput: SpeakerInput) =
        coroutineScope {
            val event = eventDao.get(eventId)
            val speakerDb = speakerInput.convertToDb(photoUrl = speakerInput.photoUrl)
            val id = speakerDao.createOrUpdate(eventId, speakerDb)
            val avatar = commonApi.fetchByteArray(speakerInput.photoUrl)
            val bucketItem = speakerDao.saveProfile(
                eventId = eventId,
                id = id,
                content = avatar,
                mimeType = speakerInput.photoUrl.mimeType
            )
            speakerDao.createOrUpdate(eventId, speakerDb.copy(photoUrl = bucketItem.url))
            eventDao.updateAgendaUpdatedAt(event)
            return@coroutineScope id
        }

    suspend fun update(eventId: String, speakerId: String, input: SpeakerInput) = coroutineScope {
        val event = eventDao.get(eventId)
        val avatar = commonApi.fetchByteArray(input.photoUrl)
        val bucketItem = speakerDao.saveProfile(
            eventId = eventId,
            id = speakerId,
            content = avatar,
            mimeType = input.photoUrl.mimeType
        )
        val speakerDb = input.convertToDb(photoUrl = bucketItem.url, speakerId)
        eventDao.updateAgendaUpdatedAt(event)
        return@coroutineScope speakerDao.createOrUpdate(eventId, speakerDb)
    }
}
