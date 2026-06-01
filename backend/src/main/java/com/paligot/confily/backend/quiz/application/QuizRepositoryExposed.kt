package com.paligot.confily.backend.quiz.application

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.quiz.domain.QuizRepository
import com.paligot.confily.backend.quiz.infrastructure.exposed.QuizPlayerEntity
import com.paligot.confily.models.LeaderboardEntry
import com.paligot.confily.models.QuizQuestion
import com.paligot.confily.models.QuizSubmissionResult
import com.paligot.confily.models.inputs.QuizPlayerInput
import com.paligot.confily.models.inputs.QuizSubmissionInput
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.Database
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

    override suspend fun questionsByCode(eventId: String, code: String): List<QuizQuestion> = TODO()

    override suspend fun submit(
        eventId: String,
        code: String,
        input: QuizSubmissionInput
    ): QuizSubmissionResult = TODO()

    override suspend fun leaderboard(eventId: String): List<LeaderboardEntry> = TODO()
}
