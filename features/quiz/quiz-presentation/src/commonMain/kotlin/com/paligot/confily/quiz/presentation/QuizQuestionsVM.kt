package com.paligot.confily.quiz.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.paligot.confily.core.api.QuizException
import com.paligot.confily.quiz.panes.QuizQuestionsPane
import com.paligot.confily.resources.Resource
import com.paligot.confily.resources.quiz_error_code_not_found
import com.paligot.confily.resources.text_error
import com.paligot.confily.resources.text_loading
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun QuizQuestionsVM(
    code: String,
    onSubmitted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuizQuestionsViewModel = koinViewModel()
) {
    LaunchedEffect(code) { viewModel.load(code) }
    when (val state = viewModel.uiState.collectAsState().value) {
        is QuizQuestionsUiState.Loading -> Text(text = stringResource(Resource.string.text_loading))
        is QuizQuestionsUiState.Failure -> Text(
            text = stringResource(
                if (state.throwable is QuizException.CodeNotFound) {
                    Resource.string.quiz_error_code_not_found
                } else {
                    Resource.string.text_error
                }
            )
        )
        is QuizQuestionsUiState.Success -> QuizQuestionsPane(
            questions = state.questions,
            selections = state.selections,
            onSelect = viewModel::select,
            onSubmit = { viewModel.submit(code) },
            modifier = modifier
        )
        is QuizQuestionsUiState.Submitted -> LaunchedEffect(Unit) { onSubmitted() }
        is QuizQuestionsUiState.AlreadySubmitted -> LaunchedEffect(Unit) { onSubmitted() }
    }
}
