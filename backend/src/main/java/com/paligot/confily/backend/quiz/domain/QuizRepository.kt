package com.paligot.confily.backend.quiz.domain

import com.paligot.confily.models.LeaderboardEntry
import com.paligot.confily.models.QuizQuestion
import com.paligot.confily.models.QuizSubmissionResult
import com.paligot.confily.models.inputs.QuizPlayerInput
import com.paligot.confily.models.inputs.QuizSubmissionInput

interface QuizRepository {
    suspend fun registerPlayer(eventId: String, input: QuizPlayerInput): String
    suspend fun questionsByCode(eventId: String, code: String): List<QuizQuestion>
    suspend fun submit(eventId: String, code: String, input: QuizSubmissionInput): QuizSubmissionResult
    suspend fun leaderboard(eventId: String): List<LeaderboardEntry>
}
