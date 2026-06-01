package com.paligot.confily.backend.quiz

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerEntity
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizAnswerEntity
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizQuestionEntity
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object QuizTestFixtures {
    fun createEvent(database: Database, slug: String = "event-${UUID.randomUUID()}"): UUID =
        transaction(database) {
            EventEntity.new {
                this.slug = slug
                this.name = "Test Event"
                this.startDate = LocalDate(2026, 6, 1)
                this.endDate = LocalDate(2026, 6, 2)
            }.id.value
        }

    fun createPartner(database: Database, eventId: UUID, quizCode: String? = null): UUID =
        transaction(database) {
            PartnerEntity.new {
                this.event = EventEntity[eventId]
                this.name = "Partner ${UUID.randomUUID()}"
                this.quizCode = quizCode
            }.id.value
        }

    @Suppress("LongParameterList")
    fun createQuestion(
        database: Database,
        eventId: UUID,
        partnerId: UUID,
        question: String,
        options: List<String>,
        correctIndex: Int,
        order: Int = 0
    ): UUID = transaction(database) {
        val questionEntity = QuizQuestionEntity.new {
            this.event = EventEntity[eventId]
            this.partner = PartnerEntity[partnerId]
            this.question = question
            this.displayOrder = order
        }
        options.forEachIndexed { index, label ->
            QuizAnswerEntity.new {
                this.question = questionEntity
                this.answer = label
                this.isCorrect = index == correctIndex
                this.displayOrder = index
            }
        }
        questionEntity.id.value
    }

    fun correctAnswerId(database: Database, questionId: UUID): UUID = transaction(database) {
        QuizAnswerEntity.findByQuestion(questionId).first { it.isCorrect }.id.value
    }

    fun answerIds(database: Database, questionId: UUID): List<UUID> = transaction(database) {
        QuizAnswerEntity.findByQuestion(questionId).map { it.id.value }
    }
}
