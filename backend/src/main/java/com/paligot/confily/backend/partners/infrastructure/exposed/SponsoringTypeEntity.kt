package com.paligot.confily.backend.partners.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class SponsoringTypeEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SponsoringTypeEntity>(SponsoringTypesTable) {
        fun findByEvent(eventId: UUID): SizedIterable<SponsoringTypeEntity> = this
            .find { SponsoringTypesTable.eventId eq eventId }

        fun findByTypeNames(eventId: UUID, typeNames: List<String>): SizedIterable<SponsoringTypeEntity> = this
            .find { (SponsoringTypesTable.eventId eq eventId) and (SponsoringTypesTable.typeName inList typeNames) }
    }

    var eventId by SponsoringTypesTable.eventId
    var event by EventEntity.Companion referencedOn SponsoringTypesTable.eventId
    var typeName by SponsoringTypesTable.typeName
    var displayOrder by SponsoringTypesTable.displayOrder
    var createdAt by SponsoringTypesTable.createdAt
}
