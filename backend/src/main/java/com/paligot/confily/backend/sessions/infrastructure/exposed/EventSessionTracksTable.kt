package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object EventSessionTracksTable : UUIDTable("event_session_tracks") {
    val eventId = reference("event_id", EventsTable).index()
    val trackName = varchar("track_name", 255)
    val displayOrder = integer("display_order").default(0)
    val externalId = varchar("external_id", 255).nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }

    init {
        uniqueIndex(eventId, trackName)
        uniqueIndex(eventId, externalId)
    }
}
