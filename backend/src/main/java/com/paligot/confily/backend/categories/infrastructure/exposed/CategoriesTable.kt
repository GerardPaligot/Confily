package com.paligot.confily.backend.categories.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object CategoriesTable : UUIDTable("categories") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE).index()
    val name = varchar("name", 255)
    val icon = varchar("icon", 50).nullable()
    val color = varchar("color", 20).nullable()
    val displayOrder = integer("display_order").default(0)
    val externalId = varchar("external_id", 255).nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        uniqueIndex(eventId, name)
        uniqueIndex(eventId, externalId)
        index(isUnique = false, eventId, displayOrder)
    }
}
