package com.paligot.confily.quiz.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.paligot.confily.quiz.routes.QuizHome
import com.paligot.confily.quiz.routes.QuizQuestions
import com.paligot.confily.quiz.routes.QuizResult

fun NavGraphBuilder.quizGraph(navController: NavController) {
    composable<QuizHome> {
        QuizHomeVM(onStart = { code -> navController.navigate(QuizQuestions(code)) })
    }
    composable<QuizQuestions> { entry ->
        val code = entry.toRoute<QuizQuestions>().code
        QuizQuestionsVM(
            code = code,
            onSubmitted = {
                navController.navigate(QuizResult(code)) {
                    popUpTo(QuizHome) {
                        inclusive = false
                    }
                }
            }
        )
    }
    composable<QuizResult> { entry ->
        val code = entry.toRoute<QuizResult>().code
        QuizResultVM(code = code, onDone = { navController.popBackStack(QuizHome, inclusive = false) })
    }
}
