package com.paligot.confily.backend.tags.application

import com.paligot.confily.backend.tags.domain.TagRepository
import com.paligot.confily.backend.tags.infrastructure.firestore.TagEntity
import com.paligot.confily.backend.tags.infrastructure.firestore.TagFirestore
import com.paligot.confily.backend.tags.infrastructure.firestore.convertToModel
import com.paligot.confily.models.Tag

class TagRepositoryDefault(
    private val tagFirestore: TagFirestore
) : TagRepository {
    override suspend fun list(eventId: String): List<Tag> = tagFirestore
        .getAll(eventId)
        .map(TagEntity::convertToModel)
}
