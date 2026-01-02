package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.addresses.infrastructure.exposed.AddressesTable
import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object EventSessionsTable : UUIDTable("event_sessions") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.RESTRICT).index()
    val title = varchar("title", 500)
    val description = text("description").nullable()
    val addressId = reference("address_id", AddressesTable).nullable()
    val externalId = varchar("external_id", 255).nullable().uniqueIndex()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }
}
