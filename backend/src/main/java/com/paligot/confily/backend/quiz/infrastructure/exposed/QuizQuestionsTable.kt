package com.paligot.confily.backend.quiz.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnersTable
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object QuizQuestionsTable : UUIDTable("quiz_questions") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE)
    val partnerId = reference("partner_id", PartnersTable, onDelete = ReferenceOption.CASCADE)
    val displayOrder = integer("display_order").default(0)
    val question = text("question")
    val externalId = varchar("external_id", 255).nullable()
    val externalProvider = enumeration<IntegrationProvider>("external_provider").nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, partnerId)
        index(isUnique = false, eventId)
        uniqueIndex(eventId, externalId, externalProvider)
    }
}
