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
        val deviceHash = DeviceIdHasher.hash(input.deviceId)
        return transaction(db = database) {
            val event = EventEntity[eventUuid]
            val existing = QuizPlayerEntity.findByDevice(eventUuid, deviceHash)
            val player = if (existing != null) {
                existing.username = input.username
                existing.updatedAt = Clock.System.now()
                existing
            } else {
                QuizPlayerEntity.new {
                    this.event = event
                    this.deviceId = deviceHash
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
    ): QuizSubmissionResult {
        val eventUuid = UUID.fromString(eventId)
        val deviceHash = DeviceIdHasher.hash(input.deviceId)
        return transaction(db = database) {
            val partner = PartnerEntity.findByQuizCode(eventUuid, code)
                ?: throw NotFoundException("No partner found for code $code")
            val player = QuizPlayerEntity.findByDevice(eventUuid, deviceHash)
                ?: throw NotFoundException("Player not registered for this device")
            val alreadySubmitted = QuizSubmissionEntity
                .findByPlayerAndPartner(player.id.value, partner.id.value)
            if (alreadySubmitted != null) {
                throw ConflictException("Answers already submitted for this partner")
            }

            val chosenByQuestion = input.answers
                .associate { UUID.fromString(it.questionId) to UUID.fromString(it.answerId) }
            val questions = QuizQuestionEntity.findByPartner(partner.id.value).toList()

            val graded = questions.map { question ->
                val correct = QuizAnswerEntity.findByQuestion(question.id.value)
                    .first { it.isCorrect }
                val chosen = chosenByQuestion[question.id.value]
                val isCorrect = chosen != null && chosen == correct.id.value
                GradedAnswer(question.id.value, chosen, correct.id.value, isCorrect)
            }
            val correctCount = graded.count { it.isCorrect }

            val submission = QuizSubmissionEntity.new {
                this.event = partner.event
                this.player = player
                this.partner = partner
                this.correctCount = correctCount
                this.totalCount = questions.size
            }
            graded.forEach { answer ->
                if (answer.chosenAnswerId != null) {
                    QuizSubmissionAnswerEntity.new {
                        this.submission = submission
                        this.questionId = EntityID(answer.questionId, QuizQuestionsTable)
                        this.chosenAnswerId = EntityID(answer.chosenAnswerId, QuizAnswersTable)
                        this.isCorrect = answer.isCorrect
                    }
                }
            }

            QuizSubmissionResult(
                correctCount = correctCount,
                totalCount = questions.size,
                perQuestion = graded.map { answer ->
                    QuizAnsweredQuestion(
                        questionId = answer.questionId.toString(),
                        chosenAnswerId = answer.chosenAnswerId?.toString(),
                        correctAnswerId = answer.correctAnswerId.toString(),
                        isCorrect = answer.isCorrect
                    )
                }
            )
        }
    }

    override suspend fun leaderboard(eventId: String): List<LeaderboardEntry> {
        val eventUuid = UUID.fromString(eventId)
        return transaction(db = database) {
            QuizSubmissionEntity.findByEvent(eventUuid)
                .groupBy { it.playerId.value }
                .map { (playerId, submissions) ->
                    PlayerScore(
                        username = QuizPlayerEntity[playerId].username,
                        score = submissions.sumOf { it.correctCount },
                        lastSubmittedAt = submissions.maxOf { it.submittedAt }
                    )
                }
                .sortedWith(compareByDescending<PlayerScore> { it.score }.thenBy { it.lastSubmittedAt })
                .mapIndexed { index, score ->
                    LeaderboardEntry(rank = index + 1, username = score.username, score = score.score)
                }
        }
    }

    private data class GradedAnswer(
        val questionId: UUID,
        val chosenAnswerId: UUID?,
        val correctAnswerId: UUID,
        val isCorrect: Boolean
    )

    private data class PlayerScore(
        val username: String,
        val score: Int,
        val lastSubmittedAt: Instant
    )
}
