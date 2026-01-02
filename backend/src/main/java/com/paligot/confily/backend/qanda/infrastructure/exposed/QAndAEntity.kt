package com.paligot.confily.backend.qanda.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class QAndAEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<QAndAEntity>(QAndATable) {
        fun findByEvent(eventId: UUID): SizedIterable<QAndAEntity> = this
            .find { QAndATable.eventId eq eventId }

        fun findByLanguage(eventId: UUID, language: String): SizedIterable<QAndAEntity> = this
            .find { (QAndATable.eventId eq eventId) and (QAndATable.language eq language) }

        fun findById(eventId: UUID, qandaId: UUID): QAndAEntity? = this
            .find { (QAndATable.eventId eq eventId) and (QAndATable.id eq qandaId) }
            .firstOrNull()

        fun findByExternalId(eventId: UUID, externalId: String): QAndAEntity? = this
            .find { (QAndATable.eventId eq eventId) and (QAndATable.externalId eq externalId) }
            .firstOrNull()
    }

    var eventId by QAndATable.eventId
    var event by EventEntity.Companion referencedOn QAndATable.eventId
    var displayOrder by QAndATable.displayOrder
    var language by QAndATable.language
    var question by QAndATable.question
    var response by QAndATable.response
    var externalId by QAndATable.externalId
    var createdAt by QAndATable.createdAt
    var updatedAt by QAndATable.updatedAt
}
