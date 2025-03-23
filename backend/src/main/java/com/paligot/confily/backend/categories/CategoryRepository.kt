package com.paligot.confily.backend.categories

import com.paligot.confily.models.inputs.CategoryInput
import kotlinx.coroutines.coroutineScope

class CategoryRepository(
    private val categoryDao: CategoryDao
) {
    suspend fun list(eventId: String) = coroutineScope {
        return@coroutineScope categoryDao.getAll(eventId).map { it.convertToModel() }
    }

    suspend fun create(eventId: String, category: CategoryInput) = coroutineScope {
        categoryDao.createOrUpdate(eventId, category.convertToDb())
        return@coroutineScope eventId
    }

    suspend fun update(eventId: String, categoryId: String, input: CategoryInput) = coroutineScope {
        categoryDao.createOrUpdate(eventId, input.convertToDb(categoryId))
        return@coroutineScope eventId
    }
}
