package com.paligot.confily.backend.tags.application

import com.paligot.confily.backend.tags.domain.TagAdminRepository
import com.paligot.confily.backend.tags.infrastructure.firestore.TagFirestore
import com.paligot.confily.backend.tags.infrastructure.firestore.convertToEntity
import com.paligot.confily.models.inputs.TagInput

class TagAdminRepositoryDefault(
    private val tagFirestore: TagFirestore
) : TagAdminRepository {
    override suspend fun create(eventId: String, input: TagInput): String {
        tagFirestore.createOrUpdate(eventId, input.convertToEntity())
        return eventId
    }

    override suspend fun update(eventId: String, tagId: String, input: TagInput): String {
        tagFirestore.createOrUpdate(eventId, input.convertToEntity(tagId))
        return eventId
    }
}
