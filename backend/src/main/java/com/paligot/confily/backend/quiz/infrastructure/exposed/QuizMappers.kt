package com.paligot.confily.backend.quiz.infrastructure.exposed

import com.paligot.confily.models.QuizAnswerOption
import com.paligot.confily.models.QuizQuestion

fun QuizAnswerEntity.toOption(): QuizAnswerOption = QuizAnswerOption(
    id = this.id.value.toString(),
    label = this.answer,
    order = this.displayOrder
)

fun QuizQuestionEntity.toModel(options: List<QuizAnswerOption>): QuizQuestion = QuizQuestion(
    id = this.id.value.toString(),
    order = this.displayOrder,
    question = this.question,
    options = options
)
