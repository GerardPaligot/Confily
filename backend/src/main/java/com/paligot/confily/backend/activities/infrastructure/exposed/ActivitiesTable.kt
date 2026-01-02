package com.paligot.confily.backend.activities.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnersTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object ActivitiesTable : UUIDTable("activities") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE)
    val partnerId = reference("partner_id", PartnersTable, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 255)
    val startTime = timestamp("start_time")
    val endTime = timestamp("end_time").nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, eventId)
        index(isUnique = false, partnerId)
        index(isUnique = false, eventId, startTime)
    }
}
