package com.paligot.confily.backend.tags.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class TagEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TagEntity>(TagsTable) {
        fun findByEvent(eventId: UUID): SizedIterable<TagEntity> = this
            .find { TagsTable.eventId eq eventId }

        fun findById(eventId: UUID, tagId: UUID): TagEntity? = this
            .find { (TagsTable.eventId eq eventId) and (TagsTable.id eq tagId) }
            .firstOrNull()
    }

    var eventId by TagsTable.eventId
    var event by EventEntity.Companion referencedOn TagsTable.eventId
    var name by TagsTable.name
    var createdAt by TagsTable.createdAt
    var updatedAt by TagsTable.updatedAt
}
