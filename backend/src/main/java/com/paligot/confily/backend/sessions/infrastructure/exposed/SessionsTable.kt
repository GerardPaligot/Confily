package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatsTable
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

object SessionsTable : UUIDTable("sessions") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE).index()
    val formatId = reference("format_id", FormatsTable, onDelete = ReferenceOption.SET_NULL).nullable().index()
    val title = varchar("title", 500)
    val description = text("description").nullable()
    val language = varchar("language", 10).default("en")
    val level = varchar("level", 50).nullable()
    val linkSlides = text("link_slides").nullable()
    val linkReplay = text("link_replay").nullable()
    val driveFolderId = text("drive_folder_id").nullable()
    val externalId = varchar("external_id", 255).nullable()
    val externalProvider = enumeration<IntegrationProvider>("external_provider").nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, language)
        index(isUnique = false, level)
        uniqueIndex(eventId, externalId, externalProvider)
    }

    fun deleteDiff(
        eventId: UUID,
        externalIds: List<String>,
        provider: IntegrationProvider
    ) {
        deleteWhere {
            val eventOp = SessionsTable.eventId eq eventId
            val externalIdsOp = SessionsTable.externalId notInList externalIds
            val providerOp = SessionsTable.externalProvider eq provider
            eventOp and externalIdsOp and providerOp
        }
    }
}
