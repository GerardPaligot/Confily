package com.paligot.confily.backend.categories.application

import com.paligot.confily.backend.categories.domain.CategoryRepository
import com.paligot.confily.backend.internals.infrastructure.firestore.CategoryFirestore
import kotlinx.coroutines.coroutineScope

internal class CategoryRepositoryDefault(
    private val categoryDao: CategoryFirestore
) : CategoryRepository {
    override suspend fun list(eventId: String) = coroutineScope {
        return@coroutineScope categoryDao.getAll(eventId).map { it.convertToModel() }
    }
}
