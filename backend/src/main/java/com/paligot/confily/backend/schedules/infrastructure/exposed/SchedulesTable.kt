package com.paligot.confily.backend.schedules.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionTracksTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionsTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object SchedulesTable : UUIDTable("schedules") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.RESTRICT)
    val sessionId = reference(
        "session_id",
        SessionsTable,
        onDelete = ReferenceOption.SET_NULL
    ).nullable()
    val eventSessionId = reference(
        "event_session_id",
        EventSessionsTable,
        onDelete = ReferenceOption.SET_NULL
    ).nullable()
    val eventSessionTrackId = reference(
        "event_session_track_id",
        EventSessionTracksTable,
        onDelete = ReferenceOption.RESTRICT
    )
    val displayOrder = integer("display_order").nullable()
    val startTime = timestamp("start_time")
    val endTime = timestamp("end_time")
    val externalId = varchar("external_id", 255).nullable().uniqueIndex()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, eventId)
        index(isUnique = false, sessionId)
        index(isUnique = false, eventSessionId)
        index(isUnique = false, eventSessionTrackId)
        index(isUnique = false, eventId, startTime, endTime)
        check("check_time_order") { endTime greater startTime }
    }
}
