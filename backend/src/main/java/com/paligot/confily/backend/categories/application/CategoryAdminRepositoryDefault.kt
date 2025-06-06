package com.paligot.confily.backend.categories.application

import com.paligot.confily.backend.categories.domain.CategoryAdminRepository
import com.paligot.confily.backend.infrastructure.firestore.CategoryFirestore
import com.paligot.confily.models.inputs.CategoryInput
import kotlinx.coroutines.coroutineScope

internal class CategoryAdminRepositoryDefault(
    private val categoryDao: CategoryFirestore
): CategoryAdminRepository {
    override suspend fun create(eventId: String, category: CategoryInput) = coroutineScope {
        categoryDao.createOrUpdate(eventId, category.convertToDb())
        return@coroutineScope eventId
    }

    override suspend fun update(eventId: String, categoryId: String, input: CategoryInput) = coroutineScope {
        categoryDao.createOrUpdate(eventId, input.convertToDb(categoryId))
        return@coroutineScope eventId
    }
}
