package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object SessionVotesTable : UUIDTable("session_votes") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE).index()
    val sessionId = reference("session_id", SessionsTable, onDelete = ReferenceOption.CASCADE).index()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
}
