package com.paligot.confily.backend.tags

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.models.inputs.TagInput
import kotlinx.coroutines.coroutineScope

class TagRepository(
    private val eventDao: EventDao,
    private val tagDao: TagDao
) {
    suspend fun list(eventId: String) = coroutineScope {
        return@coroutineScope tagDao.getAll(eventId)
            .map { it.convertToModel() }
    }

    suspend fun get(eventId: String, tagId: String) = coroutineScope {
        return@coroutineScope tagDao.get(eventId, tagId)?.convertToModel()
            ?: throw NotFoundException("Category $tagId Not Found")
    }

    suspend fun create(eventId: String, apiKey: String, input: TagInput) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        tagDao.createOrUpdate(eventId, input.convertToDb())
        eventDao.updateUpdatedAt(event)
        return@coroutineScope eventId
    }

    suspend fun update(
        eventId: String,
        apiKey: String,
        tagId: String,
        input: TagInput
    ) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        tagDao.createOrUpdate(eventId, input.convertToDb(tagId))
        eventDao.updateUpdatedAt(event)
        return@coroutineScope eventId
    }
}
