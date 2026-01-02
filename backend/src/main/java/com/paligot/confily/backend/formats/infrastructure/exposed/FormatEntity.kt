package com.paligot.confily.backend.formats.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class FormatEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<FormatEntity>(FormatsTable) {
        fun findByEvent(eventId: UUID): SizedIterable<FormatEntity> = this
            .find { FormatsTable.eventId eq eventId }

        fun findByExternalId(eventId: UUID, externalId: String): FormatEntity? = this
            .find { (FormatsTable.eventId eq eventId) and (FormatsTable.externalId eq externalId) }
            .firstOrNull()
    }

    var eventId by FormatsTable.eventId
    var event by EventEntity.Companion referencedOn FormatsTable.eventId
    var name by FormatsTable.name
    var externalId by FormatsTable.externalId
    var createdAt by FormatsTable.createdAt
    var updatedAt by FormatsTable.updatedAt
}
