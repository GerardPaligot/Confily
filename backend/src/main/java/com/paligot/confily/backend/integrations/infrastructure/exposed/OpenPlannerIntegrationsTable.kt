package com.paligot.confily.backend.integrations.infrastructure.exposed

import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import io.ktor.server.plugins.NotFoundException
import org.jetbrains.exposed.crypt.encryptedVarchar
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

object OpenPlannerIntegrationsTable : Table("openplanner_integrations") {
    val integrationId = reference("integration_id", IntegrationsTable)
    val eventId = encryptedVarchar(
        name = "event_id",
        cipherTextLength = 255,
        encryptor = SystemEnv.Crypto.algorithm
    )
    val apiKey = encryptedVarchar(
        name = "api_key",
        cipherTextLength = 255,
        encryptor = SystemEnv.Crypto.algorithm
    )

    override val primaryKey = PrimaryKey(integrationId)
}

data class OpenPlannerConfig(val eventId: String, val apiKey: String)

operator fun OpenPlannerIntegrationsTable.get(integrationId: UUID): OpenPlannerConfig = OpenPlannerIntegrationsTable
    .selectAll()
    .where { OpenPlannerIntegrationsTable.integrationId eq integrationId }
    .map {
        OpenPlannerConfig(
            eventId = it[OpenPlannerIntegrationsTable.eventId],
            apiKey = it[OpenPlannerIntegrationsTable.apiKey]
        )
    }
    .singleOrNull()
    ?: throw NotFoundException("OpenPlanner config not found")
