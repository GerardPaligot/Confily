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

class BilletWebRegistrar(
    private val database: Database
) : IntegrationRegistrar<CreateIntegration.CreateBilletWebIntegration> {
    override val supportedUsages = setOf(IntegrationUsage.TICKETING)

    override fun register(
        eventId: String,
        usage: IntegrationUsage,
        input: CreateIntegration.CreateBilletWebIntegration
    ): String = transaction(db = database) {
        val integrationId = IntegrationsTable.insertAndGetId {
            it[this.eventId] = UUID.fromString(eventId)
            it[this.provider] = IntegrationProvider.BILLETWEB
            it[this.usage] = usage
        }
        BilletWebIntegrationsTable.insert {
            it[this.integrationId] = integrationId.value
            it[this.basic] = input.basic
            it[this.eventId] = input.eventId
            it[this.rateId] = input.rateId
        }
        integrationId.value.toString()
    }

    override fun unregister(integrationId: String): Unit = transaction(db = database) {
        val integrationUuid = UUID.fromString(integrationId)
        BilletWebIntegrationsTable.deleteWhere { BilletWebIntegrationsTable.integrationId eq integrationUuid }
        IntegrationsTable.deleteWhere { IntegrationsTable.id eq integrationUuid }
    }

    override fun supports(input: CreateIntegration): Boolean = input is CreateIntegration.CreateBilletWebIntegration
}
