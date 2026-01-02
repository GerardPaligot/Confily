package com.paligot.confily.backend.integrations.infrastructure.exposed

import com.paligot.confily.backend.integrations.domain.CreateIntegration
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.integrations.domain.IntegrationRegistrar
import com.paligot.confily.backend.integrations.domain.IntegrationUsage
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class WebhookRegistrar(
    private val database: Database
) : IntegrationRegistrar<CreateIntegration.CreateWebhookIntegration> {
    override val supportedUsages = setOf(IntegrationUsage.WEBHOOK)

    override fun register(
        eventId: String,
        usage: IntegrationUsage,
        input: CreateIntegration.CreateWebhookIntegration
    ): String = transaction(db = database) {
        val integrationId = IntegrationsTable.insertAndGetId {
            it[this.eventId] = UUID.fromString(eventId)
            it[this.provider] = IntegrationProvider.WEBHOOK
            it[this.usage] = usage
        }

        WebhookIntegrationsTable.insert {
            it[this.integrationId] = integrationId.value
            it[this.url] = input.url
            it[this.healthUrl] = input.healthUrl
            it[this.headerAuth] = input.headerAuth
        }

        integrationId.value.toString()
    }

    override fun unregister(integrationId: String): Unit = transaction(db = database) {
        val integrationUuid = UUID.fromString(integrationId)
        WebhookIntegrationsTable.deleteWhere { WebhookIntegrationsTable.integrationId eq integrationUuid }
        IntegrationsTable.deleteWhere { IntegrationsTable.id eq integrationUuid }
    }

    override fun supports(input: CreateIntegration): Boolean = input is CreateIntegration.CreateWebhookIntegration
}
