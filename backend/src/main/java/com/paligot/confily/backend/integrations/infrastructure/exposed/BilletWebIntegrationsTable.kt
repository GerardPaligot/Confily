package com.paligot.confily.backend.integrations.infrastructure.exposed

import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import io.ktor.server.plugins.NotFoundException
import org.jetbrains.exposed.crypt.encryptedVarchar
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import java.util.UUID

object BilletWebIntegrationsTable : Table("billetweb_integrations") {
    val integrationId = reference("integration_id", IntegrationsTable, onDelete = ReferenceOption.CASCADE)
    val basic = encryptedVarchar(
        name = "basic",
        cipherTextLength = 255,
        encryptor = SystemEnv.Crypto.algorithm
    )
    val eventId = varchar("event_id", length = 50)
    val rateId = varchar("rate_id", length = 50)

    override val primaryKey = PrimaryKey(integrationId)
}

data class BilletWebConfig(val basic: String, val eventId: String, val rateId: String)

operator fun BilletWebIntegrationsTable.get(integrationId: UUID): BilletWebConfig = BilletWebIntegrationsTable
    .selectAll()
    .where { BilletWebIntegrationsTable.integrationId eq integrationId }
    .map {
        BilletWebConfig(
            basic = it[BilletWebIntegrationsTable.basic],
            eventId = it[BilletWebIntegrationsTable.eventId],
            rateId = it[BilletWebIntegrationsTable.rateId]
        )
    }
    .singleOrNull()
    ?: throw NotFoundException("BilletWeb config not found")
