package com.paligot.confily.backend.qanda.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object QAndATable : UUIDTable("qanda") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.RESTRICT)
    val displayOrder = integer("display_order").default(0)
    val language = varchar("language", 10)
    val question = text("question")
    val response = text("response")
    val externalId = varchar("external_id", 255).nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, eventId)
        index(isUnique = false, language)
        index(isUnique = false, eventId, displayOrder)
        uniqueIndex(eventId, externalId)
    }
}
