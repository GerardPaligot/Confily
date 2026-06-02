package com.paligot.confily.backend.quiz.infrastructure.exposed

import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class QuizAnswerEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<QuizAnswerEntity>(QuizAnswersTable) {
        fun findByQuestion(questionId: UUID): SizedIterable<QuizAnswerEntity> = this
            .find { QuizAnswersTable.questionId eq questionId }

        fun findByExternalId(
            questionId: UUID,
            externalId: String,
            provider: IntegrationProvider
        ): QuizAnswerEntity? = this.find {
            (QuizAnswersTable.questionId eq questionId) and
                (QuizAnswersTable.externalId eq externalId) and
                (QuizAnswersTable.externalProvider eq provider)
        }.firstOrNull()
    }

    var questionId by QuizAnswersTable.questionId
    var question by QuizQuestionEntity referencedOn QuizAnswersTable.questionId
    var answer by QuizAnswersTable.answer
    var isCorrect by QuizAnswersTable.isCorrect
    var displayOrder by QuizAnswersTable.displayOrder
    var externalId by QuizAnswersTable.externalId
    var externalProvider by QuizAnswersTable.externalProvider
    var createdAt by QuizAnswersTable.createdAt
}
