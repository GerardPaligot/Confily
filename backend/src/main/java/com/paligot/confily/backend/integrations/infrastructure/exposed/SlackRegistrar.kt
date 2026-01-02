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

class SlackRegistrar(
    private val database: Database
) : IntegrationRegistrar<CreateIntegration.CreateSlackIntegration> {
    override val supportedUsages = setOf(IntegrationUsage.NOTIFICATION)

    override fun register(
        eventId: String,
        usage: IntegrationUsage,
        input: CreateIntegration.CreateSlackIntegration
    ): String = transaction(db = database) {
        val integrationId = IntegrationsTable.insertAndGetId {
            it[this.eventId] = UUID.fromString(eventId)
            it[this.provider] = IntegrationProvider.SLACK
            it[this.usage] = usage
        }
        SlackIntegrationsTable.insert {
            it[this.integrationId] = integrationId.value
            it[this.token] = input.token
            it[this.channel] = input.channel
        }
        integrationId.value.toString()
    }

    override fun unregister(integrationId: String): Unit = transaction(db = database) {
        val integrationUuid = UUID.fromString(integrationId)
        SlackIntegrationsTable.deleteWhere { SlackIntegrationsTable.integrationId eq integrationUuid }
        IntegrationsTable.deleteWhere { IntegrationsTable.id eq integrationUuid }
    }

    override fun supports(input: CreateIntegration): Boolean = input is CreateIntegration.CreateSlackIntegration
}
