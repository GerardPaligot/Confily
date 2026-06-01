package com.paligot.confily.backend.quiz.application

import com.paligot.confily.backend.ConflictException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerEntity
import com.paligot.confily.backend.quiz.domain.QuizRepository
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizAnswerEntity
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizAnswersTable
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizPlayerEntity
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizQuestionEntity
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizQuestionsTable
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizSubmissionAnswerEntity
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizSubmissionEntity
import com.paligot.confily.backend.quiz.infrastructure.exposed.toModel
import com.paligot.confily.backend.quiz.infrastructure.exposed.toOption
import com.paligot.confily.models.LeaderboardEntry
import com.paligot.confily.models.QuizAnsweredQuestion
import com.paligot.confily.models.QuizQuestion
import com.paligot.confily.models.QuizSubmissionResult
import com.paligot.confily.models.inputs.QuizPlayerInput
import com.paligot.confily.models.inputs.QuizSubmissionInput
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class QuizRepositoryExposed(private val database: Database) : QuizRepository {

    override suspend fun registerPlayer(eventId: String, input: QuizPlayerInput): String {
        val eventUuid = UUID.fromString(eventId)
        return transaction(db = database) {
            val event = EventEntity[eventUuid]
            val existing = QuizPlayerEntity.findByDevice(eventUuid, input.deviceId)
            val player = if (existing != null) {
                existing.username = input.username
                existing.updatedAt = Clock.System.now()
                existing
            } else {
                QuizPlayerEntity.new {
                    this.event = event
                    this.deviceId = input.deviceId
                    this.username = input.username
                }
            }
            player.id.value.toString()
        }
    }

    override suspend fun questionsByCode(eventId: String, code: String): List<QuizQuestion> {
        val eventUuid = UUID.fromString(eventId)
        return transaction(db = database) {
            val partner = PartnerEntity.findByQuizCode(eventUuid, code)
                ?: throw NotFoundException("No partner found for code $code")
            QuizQuestionEntity.findByPartner(partner.id.value)
                .orderBy(QuizQuestionsTable.displayOrder to SortOrder.ASC)
                .map { question ->
                    val options = QuizAnswerEntity.findByQuestion(question.id.value)
                        .orderBy(QuizAnswersTable.displayOrder to SortOrder.ASC)
                        .map { it.toOption() }
                    question.toModel(options)
                }
        }
    }

    override suspend fun submit(
        eventId: String,
        code: String,
        input: QuizSubmissionInput
    ): QuizSubmissionResult = TODO()

    override suspend fun leaderboard(eventId: String): List<LeaderboardEntry> = TODO()
}
