package com.paligot.confily.quiz.di

import com.paligot.confily.core.di.repositoriesModule
import com.paligot.confily.quiz.presentation.QuizHomeViewModel
import com.paligot.confily.quiz.presentation.QuizQuestionsViewModel
import com.paligot.confily.quiz.presentation.QuizResultViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val quizModule = module {
    includes(repositoriesModule)
    viewModel { QuizHomeViewModel(get()) }
    viewModel { QuizQuestionsViewModel(get()) }
    viewModel { QuizResultViewModel(get()) }
}
