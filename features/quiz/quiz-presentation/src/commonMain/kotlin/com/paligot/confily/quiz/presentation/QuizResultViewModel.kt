package com.paligot.confily.quiz.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.quiz.QuizRepository
import com.paligot.confily.quiz.ui.models.QuizResultUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class QuizResultUiState {
    data object Loading : QuizResultUiState()
    data class Success(val result: QuizResultUi) : QuizResultUiState()
    data object AlreadyCompleted : QuizResultUiState()
}

class QuizResultViewModel(
    private val repository: QuizRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<QuizResultUiState>(QuizResultUiState.Loading)
    val uiState: StateFlow<QuizResultUiState> = _uiState

    fun load(code: String) = viewModelScope.launch {
        val saved = repository.savedResult(code)
        _uiState.value = if (saved != null) {
            val labels = runCatching {
                repository.questions(code).associate { it.id to it.question }
            }.getOrDefault(emptyMap())
            QuizResultUiState.Success(result = saved.toUi(labels))
        } else {
            QuizResultUiState.AlreadyCompleted
        }
    }
}
