package com.paligot.confily.quiz.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.quiz.panes.QuizResultPane
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.quiz_already_completed
import com.paligot.confily.resources.text_loading
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QuizResultVM(
    code: String,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuizResultViewModel = koinViewModel()
) {
    LaunchedEffect(code) { viewModel.load(code) }
    when (val state = viewModel.uiState.collectAsState().value) {
        is QuizResultUiState.Loading -> Text(text = stringResource(Resource.string.text_loading))
        is QuizResultUiState.Success -> QuizResultPane(result = state.result, onDone = onDone, modifier = modifier)
        is QuizResultUiState.AlreadyCompleted -> Text(text = stringResource(Resource.string.quiz_already_completed))
    }
}
