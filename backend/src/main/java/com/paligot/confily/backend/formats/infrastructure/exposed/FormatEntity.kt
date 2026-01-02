package com.paligot.confily.backend.formats.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
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

        fun findByExternalId(eventId: UUID, externalId: String, provider: IntegrationProvider): FormatEntity? = this
            .find {
                val eventOp = FormatsTable.eventId eq eventId
                val externalOp = FormatsTable.externalId eq externalId
                val providerOp = FormatsTable.externalProvider eq provider
                eventOp and externalOp and providerOp
            }
            .firstOrNull()
    }

    var eventId by FormatsTable.eventId
    var event by EventEntity.Companion referencedOn FormatsTable.eventId
    var name by FormatsTable.name
    var externalId by FormatsTable.externalId
    var externalProvider by FormatsTable.externalProvider
    var createdAt by FormatsTable.createdAt
    var updatedAt by FormatsTable.updatedAt
}
