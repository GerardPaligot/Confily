package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and
import java.util.UUID

class EventSessionTrackEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EventSessionTrackEntity>(EventSessionTracksTable) {
        fun findByExternalId(eventId: UUID, externalId: String): EventSessionTrackEntity? = this
            .find {
                val eventOp = EventSessionTracksTable.eventId eq eventId
                val externalIdOp = EventSessionTracksTable.externalId eq externalId
                eventOp and externalIdOp
            }
            .firstOrNull()

        fun findByTrackName(eventId: UUID, trackName: String): EventSessionTrackEntity? = this
            .find { (EventSessionTracksTable.eventId eq eventId) and (EventSessionTracksTable.trackName eq trackName) }
            .firstOrNull()
    }

    var eventId by EventSessionTracksTable.eventId
    var event by EventEntity.Companion referencedOn EventSessionTracksTable.eventId
    var trackName by EventSessionTracksTable.trackName
    var displayOrder by EventSessionTracksTable.displayOrder
    var externalId by EventSessionTracksTable.externalId
    var createdAt by EventSessionTracksTable.createdAt
}
