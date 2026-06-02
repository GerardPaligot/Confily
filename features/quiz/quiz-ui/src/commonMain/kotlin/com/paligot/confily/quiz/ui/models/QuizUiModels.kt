package com.paligot.confily.quiz.ui.models

data class QuizAnswerOptionUi(
    val id: String,
    val label: String
)

data class QuizQuestionUi(
    val id: String,
    val question: String,
    val options: List<QuizAnswerOptionUi>
)

data class QuizAnsweredQuestionUi(
    val questionId: String,
    val question: String,
    val chosenAnswerId: String?,
    val correctAnswerId: String,
    val isCorrect: Boolean
)

data class QuizResultUi(
    val correctCount: Int,
    val totalCount: Int,
    val perQuestion: List<QuizAnsweredQuestionUi>
)
