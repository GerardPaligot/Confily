package com.paligot.confily.backend.integrations.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.integrations.domain.IntegrationUsage
import io.ktor.server.plugins.NotFoundException
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class IntegrationEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<IntegrationEntity>(IntegrationsTable) {
        fun findByEvent(eventId: UUID): SizedIterable<IntegrationEntity> = this
            .find { IntegrationsTable.eventId eq eventId }

        fun findIntegration(
            eventId: UUID,
            provider: IntegrationProvider,
            usage: IntegrationUsage
        ): IntegrationEntity? =
            find {
                val eventOp = IntegrationsTable.eventId eq eventId
                val usageOp = IntegrationsTable.usage eq usage
                val providerOp = IntegrationsTable.provider eq provider
                eventOp and usageOp and providerOp
            }.firstOrNull()

        fun findByIntegrationId(eventId: UUID, integrationId: UUID): IntegrationEntity {
            val integrationEntity = this
                .find { (IntegrationsTable.id eq integrationId) and (IntegrationsTable.eventId eq eventId) }
                .firstOrNull()

            if (integrationEntity == null) {
                throw NotFoundException("Integration $integrationId not found")
            }

            return integrationEntity
        }
    }

    var eventId by IntegrationsTable.eventId
    var provider by IntegrationsTable.provider
    var usage by IntegrationsTable.usage
    var createdAt by IntegrationsTable.createdAt
    var event by EventEntity referencedOn IntegrationsTable.eventId
}
