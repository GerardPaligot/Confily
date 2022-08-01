package org.gdglille.devfest.android.data.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.gdglille.devfest.models.QuestionAndResponseUi
import org.gdglille.devfest.repositories.AgendaRepository

sealed class QAndAUiState {
    data class Loading(val qanda: List<QuestionAndResponseUi>) : QAndAUiState()
    data class Success(val qanda: List<QuestionAndResponseUi>) : QAndAUiState()
    data class Failure(val throwable: Throwable) : QAndAUiState()
}

class QAndAViewModel(private val repository: AgendaRepository) : ViewModel() {
    private val _uiState =
        MutableStateFlow<QAndAUiState>(
            QAndAUiState.Loading(
                arrayListOf(
                    QuestionAndResponseUi.fake,
                    QuestionAndResponseUi.fake
                )
            )
        )
    val uiState: StateFlow<QAndAUiState> = _uiState

    init {
        viewModelScope.launch {
            try {
                repository.qanda().collect {
                    _uiState.value = QAndAUiState.Success(it)
                }
            } catch (error: Throwable) {
                Firebase.crashlytics.recordException(error)
                _uiState.value = QAndAUiState.Failure(error)
            }
        }
    }

    fun expanded(qanda: QuestionAndResponseUi) {
        val state = _uiState.value
        if (state is QAndAUiState.Success) {
            val index = state.qanda.indexOf(qanda)
            val qandaUi = state.qanda[index]
            val qandaList = state.qanda.toMutableList()
            qandaList[index] = qandaUi.copy(expanded = qandaUi.expanded.not())
            _uiState.value = QAndAUiState.Success(qandaList)
        }
    }

    object Factory {
        fun create(repository: AgendaRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                QAndAViewModel(repository = repository) as T
        }
    }
}
