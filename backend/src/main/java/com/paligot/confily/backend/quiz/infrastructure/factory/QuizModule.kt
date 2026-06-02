package com.paligot.confily.backend.quiz.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.quiz.application.QuizRepositoryExposed

object QuizModule {
    val quizRepository by lazy {
        QuizRepositoryExposed(PostgresModule.database)
    }
}
