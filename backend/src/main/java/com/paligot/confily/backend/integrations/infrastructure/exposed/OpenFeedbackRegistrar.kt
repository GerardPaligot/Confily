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

class OpenFeedbackRegistrar(
    private val database: Database
) : IntegrationRegistrar<CreateIntegration.CreateOpenFeedbackIntegration> {
    override val supportedUsages: Set<IntegrationUsage> = setOf(IntegrationUsage.FEEDBACK)

    override fun register(
        eventId: String,
        usage: IntegrationUsage,
        input: CreateIntegration.CreateOpenFeedbackIntegration
    ): String = transaction(db = database) {
        val integrationId = IntegrationsTable.insertAndGetId {
            it[this.eventId] = UUID.fromString(eventId)
            it[this.provider] = IntegrationProvider.OPENFEEDBACK
            it[this.usage] = usage
        }
        OpenFeedbackIntegrationsTable.insert {
            it[this.integrationId] = integrationId.value
            it[this.eventId] = input.eventId
        }
        integrationId.value.toString()
    }

    override fun unregister(integrationId: String): Unit = transaction(db = database) {
        val integrationUuid = UUID.fromString(integrationId)
        OpenFeedbackIntegrationsTable.deleteWhere {
            OpenFeedbackIntegrationsTable.integrationId eq integrationUuid
        }
        IntegrationsTable.deleteWhere { IntegrationsTable.id eq integrationUuid }
    }

    override fun supports(input: CreateIntegration): Boolean =
        input is CreateIntegration.CreateOpenFeedbackIntegration
}
