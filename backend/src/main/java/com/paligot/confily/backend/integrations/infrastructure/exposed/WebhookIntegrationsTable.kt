package com.paligot.confily.backend.integrations.infrastructure.exposed

import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import io.ktor.server.plugins.NotFoundException
import org.jetbrains.exposed.crypt.encryptedVarchar
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

data class WebhookConfig(
    val url: String,
    val healthUrl: String?,
    val headerAuth: String?
)

object WebhookIntegrationsTable : Table("webhook_integrations") {
    val integrationId = reference("integration_id", IntegrationsTable)
    val url = text("url")
    val healthUrl = text("health_url").nullable()
    val headerAuth = encryptedVarchar(
        name = "header_auth",
        cipherTextLength = 1000,
        encryptor = SystemEnv.Crypto.algorithm
    ).nullable()

    override val primaryKey = PrimaryKey(integrationId)
}

operator fun WebhookIntegrationsTable.get(integrationId: UUID): WebhookConfig = WebhookIntegrationsTable
    .selectAll()
    .where { WebhookIntegrationsTable.integrationId eq integrationId }
    .map {
        WebhookConfig(
            url = it[WebhookIntegrationsTable.url],
            healthUrl = it[WebhookIntegrationsTable.healthUrl],
            headerAuth = it[WebhookIntegrationsTable.headerAuth]
        )
    }
    .singleOrNull()
    ?: throw NotFoundException("Webhook config not found")
