package com.paligot.confily.backend.categories.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class CategoryEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CategoryEntity>(CategoriesTable) {
        fun findById(eventId: UUID, categoryId: UUID): CategoryEntity? = this
            .find { (CategoriesTable.eventId eq eventId) and (CategoriesTable.id eq categoryId) }
            .firstOrNull()

        fun findByExternalId(eventId: UUID, externalId: String): CategoryEntity? = this
            .find { (CategoriesTable.eventId eq eventId) and (CategoriesTable.externalId eq externalId) }
            .firstOrNull()

        fun findByEvent(eventId: UUID): SizedIterable<CategoryEntity> = this
            .find { CategoriesTable.eventId eq eventId }
    }

    var eventId by CategoriesTable.eventId
    var event by EventEntity.Companion referencedOn CategoriesTable.eventId
    var name by CategoriesTable.name
    var icon by CategoriesTable.icon
    var color by CategoriesTable.color
    var displayOrder by CategoriesTable.displayOrder
    var externalId by CategoriesTable.externalId
    var createdAt by CategoriesTable.createdAt
    var updatedAt by CategoriesTable.updatedAt
}
