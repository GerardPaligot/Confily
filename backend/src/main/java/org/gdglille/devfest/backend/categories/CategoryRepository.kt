package org.gdglille.devfest.backend.categories

import com.paligot.confily.models.inputs.CategoryInput
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.events.EventDao

class CategoryRepository(
    private val eventDao: EventDao,
    private val categoryDao: CategoryDao
) {
    suspend fun list(eventId: String) = coroutineScope {
        return@coroutineScope categoryDao.getAll(eventId)
            .map { it.convertToModel() }
    }

    suspend fun get(eventId: String, categoryId: String) = coroutineScope {
        return@coroutineScope categoryDao.get(eventId, categoryId)?.convertToModel()
            ?: throw NotFoundException("Category $categoryId Not Found")
    }

    suspend fun create(eventId: String, apiKey: String, category: CategoryInput) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        categoryDao.createOrUpdate(eventId, category.convertToDb())
        eventDao.updateUpdatedAt(event)
        return@coroutineScope eventId
    }

    suspend fun update(
        eventId: String,
        apiKey: String,
        categoryId: String,
        categoryInput: CategoryInput
    ) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        categoryDao.createOrUpdate(eventId, categoryInput.convertToDb(categoryId))
        eventDao.updateUpdatedAt(event)
        return@coroutineScope eventId
    }
}
