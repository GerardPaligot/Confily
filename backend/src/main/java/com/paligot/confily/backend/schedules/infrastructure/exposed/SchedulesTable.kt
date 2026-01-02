package com.paligot.confily.backend.schedules.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionTracksTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionsTable
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.notInList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object SchedulesTable : UUIDTable("schedules") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE)
    val sessionId = reference(
        "session_id",
        SessionsTable,
        onDelete = ReferenceOption.CASCADE
    ).nullable()
    val eventSessionId = reference(
        "event_session_id",
        EventSessionsTable,
        onDelete = ReferenceOption.CASCADE
    ).nullable()
    val eventSessionTrackId = reference(
        "event_session_track_id",
        EventSessionTracksTable,
        onDelete = ReferenceOption.CASCADE
    )
    val displayOrder = integer("display_order").nullable()
    val startTime = timestamp("start_time")
    val endTime = timestamp("end_time")
    val externalId = varchar("external_id", 255).nullable()
    val externalProvider = enumeration<IntegrationProvider>("external_provider").nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, eventId)
        index(isUnique = false, sessionId)
        index(isUnique = false, eventSessionId)
        index(isUnique = false, eventSessionTrackId)
        index(isUnique = false, eventId, startTime, endTime)
        uniqueIndex(eventId, externalId, externalProvider)
        check("check_time_order") { endTime greater startTime }
    }

    fun deleteDiff(
        eventId: UUID,
        externalIds: List<String>,
        provider: IntegrationProvider
    ) {
        deleteWhere {
            val eventOp = SchedulesTable.eventId eq eventId
            val externalIdsOp = SchedulesTable.externalId notInList externalIds
            val providerOp = SchedulesTable.externalProvider eq provider
            eventOp and externalIdsOp and providerOp
        }
    }
}
