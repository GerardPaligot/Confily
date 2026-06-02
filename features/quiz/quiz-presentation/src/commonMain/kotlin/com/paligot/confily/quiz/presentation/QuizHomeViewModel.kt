package com.paligot.confily.quiz.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.quiz.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuizHomeViewModel(
    private val repository: QuizRepository
) : ViewModel() {
    val username: StateFlow<String?> = repository.storedUsername()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val _score = MutableStateFlow<Int?>(null)
    val score: StateFlow<Int?> = _score

    init {
        viewModelScope.launch {
            runCatching { repository.cumulativeScore() }.getOrNull()?.let { _score.value = it }
        }
    }

    fun register(name: String) = viewModelScope.launch { runCatching { repository.register(name) } }
}
