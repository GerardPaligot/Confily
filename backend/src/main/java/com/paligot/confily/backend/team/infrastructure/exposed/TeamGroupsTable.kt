package com.paligot.confily.backend.team.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object TeamGroupsTable : UUIDTable("team_groups") {
    val eventId = reference("event_id", EventsTable).index()
    val name = varchar("name", 255)
    val displayOrder = integer("display_order").default(0)
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }

    init {
        uniqueIndex(eventId, name)
    }
}
