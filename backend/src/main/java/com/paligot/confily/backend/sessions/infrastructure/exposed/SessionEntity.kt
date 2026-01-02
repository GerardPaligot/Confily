package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class SessionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SessionEntity>(SessionsTable) {
        fun findByEvent(eventId: UUID): SizedIterable<SessionEntity> = this
            .find { SessionsTable.eventId eq eventId }

        fun findByDriveFolderId(eventId: UUID, driveFolderId: String?): SizedIterable<SessionEntity> = this
            .find { (SessionsTable.eventId eq eventId) and (SessionsTable.driveFolderId eq driveFolderId) }

        fun findById(eventId: UUID, sessionId: UUID): SessionEntity? = this
            .find { (SessionsTable.eventId eq eventId) and (SessionsTable.id eq sessionId) }
            .firstOrNull()

        fun findByExternalId(eventId: UUID, externalId: String, provider: IntegrationProvider): SessionEntity? = this
            .find {
                val eventOp = SessionsTable.eventId eq eventId
                val externalIdOp = SessionsTable.externalId eq externalId
                val providerOp = SessionsTable.externalProvider eq provider
                eventOp and externalIdOp and providerOp
            }
            .firstOrNull()
    }

    var eventId by SessionsTable.eventId
    var event by EventEntity referencedOn SessionsTable.eventId
    var formatId by SessionsTable.formatId
    var format by FormatEntity optionalReferencedOn SessionsTable.formatId
    var title by SessionsTable.title
    var description by SessionsTable.description
    var language by SessionsTable.language
    var level by SessionsTable.level
    var linkSlides by SessionsTable.linkSlides
    var linkReplay by SessionsTable.linkReplay
    var driveFolderId by SessionsTable.driveFolderId
    var externalId by SessionsTable.externalId
    var externalProvider by SessionsTable.externalProvider
    var createdAt by SessionsTable.createdAt
    var updatedAt by SessionsTable.updatedAt
}
