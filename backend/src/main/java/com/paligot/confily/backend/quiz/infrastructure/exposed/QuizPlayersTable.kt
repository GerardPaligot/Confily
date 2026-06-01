package com.paligot.confily.backend.quiz.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object QuizPlayersTable : UUIDTable("quiz_players") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE)
    val deviceId = varchar("device_id", 255)
    val username = varchar("username", 255)
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        uniqueIndex(eventId, deviceId)
    }
}
