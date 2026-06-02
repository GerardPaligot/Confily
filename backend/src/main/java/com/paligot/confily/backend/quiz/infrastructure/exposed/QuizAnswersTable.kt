package com.paligot.confily.backend.quiz.infrastructure.exposed

import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object QuizAnswersTable : UUIDTable("quiz_answers") {
    val questionId = reference("question_id", QuizQuestionsTable, onDelete = ReferenceOption.CASCADE)
    val answer = text("answer")
    val isCorrect = bool("is_correct").default(false)
    val displayOrder = integer("display_order").default(0)
    val externalId = varchar("external_id", 255).nullable()
    val externalProvider = enumeration<IntegrationProvider>("external_provider").nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, questionId)
        uniqueIndex(questionId, externalId, externalProvider)
    }
}
