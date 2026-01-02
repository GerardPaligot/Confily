package com.paligot.confily.backend.categories.application

import com.paligot.confily.backend.categories.domain.CategoryRepository
import com.paligot.confily.backend.categories.infrastructure.exposed.CategoriesTable
import com.paligot.confily.backend.categories.infrastructure.exposed.CategoryEntity
import com.paligot.confily.backend.categories.infrastructure.exposed.toModel
import com.paligot.confily.models.Category
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class CategoryRepositoryExposed(private val database: Database) : CategoryRepository {
    override suspend fun list(eventId: String): List<Category> = transaction(db = database) {
        CategoryEntity
            .findByEvent(UUID.fromString(eventId))
            .orderBy(CategoriesTable.displayOrder to SortOrder.ASC)
            .map { it.toModel() }
    }
}
