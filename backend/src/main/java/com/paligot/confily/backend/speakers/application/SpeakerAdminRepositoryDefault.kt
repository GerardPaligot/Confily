package com.paligot.confily.backend.speakers.application

import com.paligot.confily.backend.events.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.helpers.mimeType
import com.paligot.confily.backend.internals.infrastructure.provider.CommonApi
import com.paligot.confily.backend.speakers.domain.SpeakerAdminRepository
import com.paligot.confily.backend.speakers.infrastructure.firestore.SpeakerFirestore
import com.paligot.confily.backend.speakers.infrastructure.storage.SpeakerStorage
import com.paligot.confily.models.inputs.SpeakerInput

class SpeakerAdminRepositoryDefault(
    private val commonApi: CommonApi,
    private val eventDao: EventFirestore,
    private val speakerFirestore: SpeakerFirestore,
    private val speakerStorage: SpeakerStorage
) : SpeakerAdminRepository {
    override suspend fun create(eventId: String, speakerInput: SpeakerInput): String {
        val event = eventDao.get(eventId)
        val speakerDb = speakerInput.convertToEntity(photoUrl = speakerInput.photoUrl)
        val id = speakerFirestore.createOrUpdate(eventId, speakerDb)
        val avatar = commonApi.fetchByteArray(speakerInput.photoUrl)
        val bucketItem = speakerStorage.saveProfile(
            eventId = eventId,
            id = id,
            content = avatar,
            mimeType = speakerInput.photoUrl.mimeType
        )
        speakerFirestore.createOrUpdate(eventId, speakerDb.copy(photoUrl = bucketItem.url))
        eventDao.updateAgendaUpdatedAt(event)
        return id
    }

    override suspend fun update(eventId: String, speakerId: String, input: SpeakerInput): String {
        val event = eventDao.get(eventId)
        val avatar = commonApi.fetchByteArray(input.photoUrl)
        val bucketItem = speakerStorage.saveProfile(
            eventId = eventId,
            id = speakerId,
            content = avatar,
            mimeType = input.photoUrl.mimeType
        )
        val speakerDb = input.convertToEntity(photoUrl = bucketItem.url, speakerId)
        eventDao.updateAgendaUpdatedAt(event)
        return speakerFirestore.createOrUpdate(eventId, speakerDb)
    }
}
