package com.paligot.confily.backend.categories.application

import com.paligot.confily.backend.infrastructure.firestore.CategoryFirestore
import com.paligot.confily.backend.categories.domain.CategoryRepository
import kotlinx.coroutines.coroutineScope

internal class CategoryRepositoryDefault(
    private val categoryDao: CategoryFirestore
): CategoryRepository {
    override suspend fun list(eventId: String) = coroutineScope {
        return@coroutineScope categoryDao.getAll(eventId).map { it.convertToModel() }
    }
}
