package com.paligot.confily.backend.menus.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object LunchMenusTable : UUIDTable("lunch_menus") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE)
    val date = date("date")
    val name = varchar("name", 255)
    val dish = varchar("dish", 500)
    val accompaniment = varchar("accompaniment", 500)
    val dessert = varchar("dessert", 500)
    val displayOrder = integer("display_order").default(0)
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, eventId)
        index(isUnique = false, eventId, date)
    }
}
