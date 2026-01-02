package com.paligot.confily.backend.integrations.infrastructure.exposed

import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import io.ktor.server.plugins.NotFoundException
import org.jetbrains.exposed.crypt.encryptedVarchar
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

object SlackIntegrationsTable : Table("slack_integrations") {
    val integrationId = reference("integration_id", IntegrationsTable, onDelete = ReferenceOption.CASCADE)
    val token = encryptedVarchar(
        name = "token",
        cipherTextLength = 255,
        encryptor = SystemEnv.Crypto.algorithm
    )
    val channel = text("channel")

    override val primaryKey = PrimaryKey(integrationId)
}

data class SlackConfig(val token: String, val channel: String)

operator fun SlackIntegrationsTable.get(integrationId: UUID): SlackConfig = SlackIntegrationsTable
    .selectAll()
    .where { SlackIntegrationsTable.integrationId eq integrationId }
    .map {
        SlackConfig(
            token = it[SlackIntegrationsTable.token],
            channel = it[SlackIntegrationsTable.channel]
        )
    }
    .singleOrNull()
    ?: throw NotFoundException("Slack config not found")
