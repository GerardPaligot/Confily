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

class OpenPlannerRegistrar(
    private val database: Database
) : IntegrationRegistrar<CreateIntegration.CreateOpenPlannerIntegration> {
    override val supportedUsages: Set<IntegrationUsage> = setOf(IntegrationUsage.AGENDA)

    override fun register(
        eventId: String,
        usage: IntegrationUsage,
        input: CreateIntegration.CreateOpenPlannerIntegration
    ): String = transaction(db = database) {
        val integrationId = IntegrationsTable.insertAndGetId {
            it[this.eventId] = UUID.fromString(eventId)
            it[this.provider] = IntegrationProvider.OPENPLANNER
            it[this.usage] = usage
        }
        OpenPlannerIntegrationsTable.insert {
            it[this.integrationId] = integrationId.value
            it[this.eventId] = input.eventId
            it[this.apiKey] = input.apiKey
        }
        integrationId.value.toString()
    }

    override fun unregister(integrationId: String): Unit = transaction(db = database) {
        val integrationUuid = UUID.fromString(integrationId)
        OpenPlannerIntegrationsTable.deleteWhere { OpenPlannerIntegrationsTable.integrationId eq integrationUuid }
        IntegrationsTable.deleteWhere { IntegrationsTable.id eq integrationUuid }
    }

    override fun supports(input: CreateIntegration): Boolean = input is CreateIntegration.CreateOpenPlannerIntegration
}
