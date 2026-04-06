package com.paligot.confily.backend.integrations.infrastructure.exposed

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

object OpenFeedbackIntegrationsTable : Table("openfeedback_integrations") {
    val integrationId = reference("integration_id", IntegrationsTable, onDelete = ReferenceOption.CASCADE)
    val eventId = varchar("event_id", 255)

    override val primaryKey = PrimaryKey(integrationId)
}

data class OpenFeedbackConfig(val eventId: String)

operator fun OpenFeedbackIntegrationsTable.get(integrationId: UUID): OpenFeedbackConfig? =
    OpenFeedbackIntegrationsTable
        .selectAll()
        .where { OpenFeedbackIntegrationsTable.integrationId eq integrationId }
        .map { OpenFeedbackConfig(eventId = it[OpenFeedbackIntegrationsTable.eventId]) }
        .singleOrNull()
