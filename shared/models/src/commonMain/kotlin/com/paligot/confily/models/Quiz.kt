package com.paligot.confily.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizAnswerOption(
    val id: String,
    val label: String,
    val order: Int
)

@Serializable
data class QuizQuestion(
    val id: String,
    val order: Int,
    val question: String,
    val options: List<QuizAnswerOption>
)

@Serializable
data class QuizAnsweredQuestion(
    @SerialName("question_id")
    val questionId: String,
    @SerialName("chosen_answer_id")
    val chosenAnswerId: String?,
    @SerialName("correct_answer_id")
    val correctAnswerId: String,
    @SerialName("is_correct")
    val isCorrect: Boolean
)

@Serializable
data class QuizSubmissionResult(
    @SerialName("correct_count")
    val correctCount: Int,
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("per_question")
    val perQuestion: List<QuizAnsweredQuestion>
)

@Serializable
data class LeaderboardEntry(
    val rank: Int,
    val username: String,
    val score: Int
)
