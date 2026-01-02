package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.addresses.infrastructure.exposed.AddressEntity
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class EventSessionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EventSessionEntity>(EventSessionsTable) {
        fun findByEvent(eventId: UUID): SizedIterable<EventSessionEntity> = this
            .find { EventSessionsTable.eventId eq eventId }

        fun findById(eventId: UUID, sessionId: UUID): EventSessionEntity? = this
            .find { (EventSessionsTable.eventId eq eventId) and (EventSessionsTable.id eq sessionId) }
            .firstOrNull()

        fun findByExternalId(eventId: UUID, externalId: String, provider: IntegrationProvider): EventSessionEntity? =
            this.find {
                val eventOp = EventSessionsTable.eventId eq eventId
                val externalIdOp = EventSessionsTable.externalId eq externalId
                val providerOp = EventSessionsTable.externalProvider eq provider
                eventOp and externalIdOp and providerOp
            }.firstOrNull()
    }

    var eventId by EventSessionsTable.eventId
    var event by EventEntity referencedOn EventSessionsTable.eventId
    var title by EventSessionsTable.title
    var description by EventSessionsTable.description
    var addressId by EventSessionsTable.addressId
    var address by AddressEntity optionalReferencedOn EventSessionsTable.addressId
    var externalId by EventSessionsTable.externalId
    var externalProvider by EventSessionsTable.externalProvider
    var createdAt by EventSessionsTable.createdAt
    var updatedAt by EventSessionsTable.updatedAt
}
