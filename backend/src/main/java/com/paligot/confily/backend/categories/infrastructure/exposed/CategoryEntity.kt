package com.paligot.confily.backend.categories.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
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

        fun findByExternalId(eventId: UUID, externalId: String, provider: IntegrationProvider): CategoryEntity? = this
            .find {
                val eventOp = CategoriesTable.eventId eq eventId
                val externalIdOp = CategoriesTable.externalId eq externalId
                val providerOp = CategoriesTable.externalProvider eq provider
                eventOp and externalIdOp and providerOp
            }
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
    var externalProvider by CategoriesTable.externalProvider
    var createdAt by CategoriesTable.createdAt
    var updatedAt by CategoriesTable.updatedAt
}
