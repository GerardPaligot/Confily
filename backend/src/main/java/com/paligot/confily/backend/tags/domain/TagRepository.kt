package com.paligot.confily.backend.tags.domain

import com.paligot.confily.models.Tag

interface TagRepository {
    suspend fun list(eventId: String): List<Tag>
}
