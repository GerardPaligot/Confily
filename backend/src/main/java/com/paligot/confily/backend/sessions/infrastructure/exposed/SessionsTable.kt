package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import com.paligot.confily.backend.formats.infrastructure.exposed.FormatsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object SessionsTable : UUIDTable("sessions") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.RESTRICT).index()
    val formatId = reference("format_id", FormatsTable, onDelete = ReferenceOption.RESTRICT).nullable().index()
    val title = varchar("title", 500)
    val description = text("description").nullable()
    val language = varchar("language", 10).default("en")
    val level = varchar("level", 50).nullable()
    val linkSlides = text("link_slides").nullable()
    val linkReplay = text("link_replay").nullable()
    val driveFolderId = text("drive_folder_id").nullable()
    val externalId = varchar("external_id", 255).nullable().uniqueIndex()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, language)
        index(isUnique = false, level)
    }
}
