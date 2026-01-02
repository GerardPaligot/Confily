package com.paligot.confily.backend.categories.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.categories.domain.CategoryAdminRepository
import com.paligot.confily.backend.categories.infrastructure.exposed.CategoryEntity
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.models.inputs.CategoryInput
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

class CategoryAdminRepositoryExposed(
    private val database: Database
) : CategoryAdminRepository {
    override suspend fun create(eventId: String, category: CategoryInput): String = transaction(database) {
        val eventUuid = java.util.UUID.fromString(eventId)
        val event = EventEntity[eventUuid]
        val lastDisplayOrder = CategoryEntity
            .findByEvent(eventUuid)
            .maxByOrNull { it.displayOrder }
            ?.displayOrder
        CategoryEntity.new {
            name = category.name
            icon = category.icon
            color = category.color
            this.displayOrder = lastDisplayOrder?.let { it + 1 } ?: 0
            this.event = event
        }.id.value.toString()
    }

    override suspend fun update(eventId: String, categoryId: String, input: CategoryInput): String =
        transaction(database) {
            val eventUuid = java.util.UUID.fromString(eventId)
            val categoryUuid = java.util.UUID.fromString(categoryId)
            val category = CategoryEntity
                .findById(eventUuid, categoryUuid)
                ?: throw NotFoundException("Category not found")
            category.name = input.name
            category.icon = input.icon
            category.color = input.color
            category.id.value.toString()
        }
}
