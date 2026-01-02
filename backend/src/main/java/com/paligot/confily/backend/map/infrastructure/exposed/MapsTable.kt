package com.paligot.confily.backend.map.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object MapsTable : UUIDTable("maps") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val color = varchar("color", 7).nullable()
    val colorSelected = varchar("color_selected", 7).nullable()
    val filename = varchar("filename", 255)
    val url = text("url")
    val filledUrl = text("filled_url").nullable()
    val displayOrder = integer("display_order").default(0)
    val pictoSize = integer("picto_size").default(0)
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, eventId)
        index(isUnique = false, eventId, displayOrder)
    }
}
