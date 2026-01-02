package com.paligot.confily.backend.formats.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object FormatsTable : UUIDTable("formats") {
    val eventId = reference("event_id", EventsTable).index()
    val name = varchar("name", 255)
    val externalId = varchar("external_id", 255).nullable().uniqueIndex()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        uniqueIndex(eventId, name)
    }
}
