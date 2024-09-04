package org.gdglille.devfest.android.theme.m3.infos.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.paligot.confily.core.repositories.AgendaRepository
import com.paligot.confily.models.ui.QuestionAndResponseUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class QAndAUiState {
    data class Loading(val qanda: ImmutableList<QuestionAndResponseUi>) : QAndAUiState()
    data class Success(val qanda: ImmutableList<QuestionAndResponseUi>) : QAndAUiState()
    data class Failure(val throwable: Throwable) : QAndAUiState()
}

class QAndAListViewModel(private val repository: AgendaRepository) : ViewModel() {
    private val _uiState =
        MutableStateFlow<QAndAUiState>(
            QAndAUiState.Loading(
                persistentListOf(
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
            _uiState.value = QAndAUiState.Success(qandaList.toImmutableList())
        }
    }
}
