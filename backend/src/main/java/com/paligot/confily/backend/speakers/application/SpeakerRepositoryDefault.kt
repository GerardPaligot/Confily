package com.paligot.confily.backend.speakers.application

import com.paligot.confily.backend.internals.infrastructure.firestore.SpeakerEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.SpeakerFirestore
import com.paligot.confily.backend.speakers.domain.SpeakerRepository
import com.paligot.confily.models.Speaker

class SpeakerRepositoryDefault(
    private val speakerFirestore: SpeakerFirestore
) : SpeakerRepository {
    override suspend fun list(eventId: String): List<Speaker> = speakerFirestore
        .getAll(eventId)
        .map(SpeakerEntity::convertToModel)
}
