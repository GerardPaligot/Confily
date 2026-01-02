package com.paligot.confily.backend.integrations.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.integrations.domain.IntegrationUsage
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object IntegrationsTable : UUIDTable("integrations") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE)
    val provider = enumeration<IntegrationProvider>(name = "provider")
    val usage = enumeration<IntegrationUsage>(name = "usage")
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
}
