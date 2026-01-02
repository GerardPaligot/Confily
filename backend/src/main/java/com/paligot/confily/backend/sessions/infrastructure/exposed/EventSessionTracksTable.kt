package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.notInList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object EventSessionTracksTable : UUIDTable("event_session_tracks") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE).index()
    val trackName = varchar("track_name", 255)
    val displayOrder = integer("display_order").default(0)
    val externalId = varchar("external_id", 255).nullable()
    val externalProvider = enumeration<IntegrationProvider>("external_provider").nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }

    init {
        uniqueIndex(eventId, trackName)
        uniqueIndex(eventId, externalId, externalProvider)
    }

    fun deleteDiff(
        eventId: UUID,
        externalIds: List<String>,
        provider: IntegrationProvider
    ) {
        deleteWhere {
            val eventOp = EventSessionTracksTable.eventId eq eventId
            val externalIdsOp = EventSessionTracksTable.externalId notInList externalIds
            val providerOp = EventSessionTracksTable.externalProvider eq provider
            eventOp and externalIdsOp and providerOp
        }
    }
}
