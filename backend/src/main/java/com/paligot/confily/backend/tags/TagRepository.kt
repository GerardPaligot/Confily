package com.paligot.confily.backend.tags

import com.paligot.confily.models.inputs.TagInput
import kotlinx.coroutines.coroutineScope

class TagRepository(
    private val tagDao: TagDao
) {
    suspend fun list(eventId: String) = coroutineScope {
        return@coroutineScope tagDao.getAll(eventId)
            .map { it.convertToModel() }
    }

    suspend fun create(eventId: String, input: TagInput) = coroutineScope {
        tagDao.createOrUpdate(eventId, input.convertToDb())
        return@coroutineScope eventId
    }

    suspend fun update(eventId: String, tagId: String, input: TagInput) = coroutineScope {
        tagDao.createOrUpdate(eventId, input.convertToDb(tagId))
        return@coroutineScope eventId
    }
}
