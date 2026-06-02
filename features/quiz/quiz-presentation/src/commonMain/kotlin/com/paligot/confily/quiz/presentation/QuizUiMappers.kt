package com.paligot.confily.quiz.presentation

import com.paligot.confily.models.QuizAnsweredQuestion
import com.paligot.confily.models.QuizQuestion
import com.paligot.confily.models.QuizSubmissionResult
import com.paligot.confily.quiz.ui.models.QuizAnswerOptionUi
import com.paligot.confily.quiz.ui.models.QuizAnsweredQuestionUi
import com.paligot.confily.quiz.ui.models.QuizQuestionUi
import com.paligot.confily.quiz.ui.models.QuizResultUi

fun QuizQuestion.toUi(): QuizQuestionUi = QuizQuestionUi(
    id = id,
    question = question,
    options = options.sortedBy { it.order }.map { QuizAnswerOptionUi(id = it.id, label = it.label) }
)

fun QuizSubmissionResult.toUi(questionLabels: Map<String, String>): QuizResultUi = QuizResultUi(
    correctCount = correctCount,
    totalCount = totalCount,
    perQuestion = perQuestion.map { it.toUi(questionLabels[it.questionId] ?: "") }
)

private fun QuizAnsweredQuestion.toUi(label: String): QuizAnsweredQuestionUi = QuizAnsweredQuestionUi(
    questionId = questionId,
    question = label,
    chosenAnswerId = chosenAnswerId,
    correctAnswerId = correctAnswerId,
    isCorrect = isCorrect
)
