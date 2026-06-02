package com.paligot.confily.quiz.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.quiz.panes.QuizHomePane
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QuizHomeVM(
    onStart: (code: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuizHomeViewModel = koinViewModel()
) {
    val username = viewModel.username.collectAsState().value
    val score = viewModel.score.collectAsState().value
    QuizHomePane(
        username = username,
        score = score,
        onStart = { code, name ->
            viewModel.register(name)
            onStart(code)
        },
        modifier = modifier
    )
}
