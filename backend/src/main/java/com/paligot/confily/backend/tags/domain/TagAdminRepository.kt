package com.paligot.confily.backend.tags.domain

import com.paligot.confily.models.inputs.TagInput

interface TagAdminRepository {
    suspend fun create(eventId: String, input: TagInput): String
    suspend fun update(eventId: String, tagId: String, input: TagInput): String
}
