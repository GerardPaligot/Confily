package com.paligot.confily.quiz.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.api.QuizException
import com.paligot.confily.core.quiz.QuizRepository
import com.paligot.confily.models.inputs.QuizAnswerInput
import com.paligot.confily.quiz.ui.models.QuizQuestionUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class QuizQuestionsUiState {
    data object Loading : QuizQuestionsUiState()
    data class Success(
        val questions: List<QuizQuestionUi>,
        val selections: Map<String, String>
    ) : QuizQuestionsUiState()
    data object Submitted : QuizQuestionsUiState()
    data object AlreadySubmitted : QuizQuestionsUiState()
    data class Failure(val throwable: Throwable) : QuizQuestionsUiState()
}

class QuizQuestionsViewModel(
    private val repository: QuizRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<QuizQuestionsUiState>(QuizQuestionsUiState.Loading)
    val uiState: StateFlow<QuizQuestionsUiState> = _uiState

    fun load(code: String) = viewModelScope.launch {
        _uiState.value = QuizQuestionsUiState.Loading
        try {
            val questions = repository.questions(code).map { it.toUi() }
            _uiState.value = QuizQuestionsUiState.Success(questions = questions, selections = emptyMap())
        } catch (error: Throwable) {
            _uiState.value = QuizQuestionsUiState.Failure(error)
        }
    }

    fun select(questionId: String, answerId: String) {
        val state = _uiState.value as? QuizQuestionsUiState.Success ?: return
        _uiState.value = state.copy(selections = state.selections + (questionId to answerId))
    }

    fun submit(code: String) = viewModelScope.launch {
        val state = _uiState.value as? QuizQuestionsUiState.Success ?: return@launch
        val answers = state.selections.map { QuizAnswerInput(questionId = it.key, answerId = it.value) }
        try {
            repository.submit(code, answers)
            _uiState.value = QuizQuestionsUiState.Submitted
        } catch (_: QuizException.AlreadySubmitted) {
            _uiState.value = QuizQuestionsUiState.AlreadySubmitted
        } catch (error: Throwable) {
            _uiState.value = QuizQuestionsUiState.Failure(error)
        }
    }
}
