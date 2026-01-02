package com.paligot.confily.backend.tags.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object TagsTable : UUIDTable("tags") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.RESTRICT).index()
    val name = varchar("name", 100)
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        uniqueIndex(eventId, name)
        index(isUnique = false, name)
    }
}
