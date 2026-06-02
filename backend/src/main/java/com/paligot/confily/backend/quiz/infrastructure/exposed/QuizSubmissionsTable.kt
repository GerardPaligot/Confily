package com.paligot.confily.backend.quiz.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnersTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object QuizSubmissionsTable : UUIDTable("quiz_submissions") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE)
    val playerId = reference("player_id", QuizPlayersTable, onDelete = ReferenceOption.CASCADE)
    val partnerId = reference("partner_id", PartnersTable, onDelete = ReferenceOption.CASCADE)
    val correctCount = integer("correct_count")
    val totalCount = integer("total_count")
    val submittedAt = timestamp("submitted_at").clientDefault { Clock.System.now() }

    init {
        uniqueIndex(playerId, partnerId)
        index(isUnique = false, eventId)
    }
}
