package com.paligot.confily.infos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.events.entities.mapToQuestionAndResponseUi
import com.paligot.confily.core.extensions.fallbackIfEmpty
import com.paligot.confily.infos.ui.models.QuestionAndResponseUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

sealed class QAndAUiState {
    data class Loading(val qanda: ImmutableList<QuestionAndResponseUi>) : QAndAUiState()
    data class Success(val qanda: ImmutableList<QuestionAndResponseUi>) : QAndAUiState()
    data class Failure(val throwable: Throwable) : QAndAUiState()
}

class QAndAListViewModel(
    locale: String,
    defaultLanguage: String,
    repository: EventRepository
) : ViewModel() {
    private val _selected = MutableStateFlow<QuestionAndResponseUi?>(null)
    val uiState: StateFlow<QAndAUiState> = combine(
        flow = _selected,
        flow2 = repository.qanda(locale)
            .fallbackIfEmpty { repository.qanda(locale.split("-").first()) }
            .fallbackIfEmpty { repository.qanda(defaultLanguage) },
        transform = { selected, qanda ->
            QAndAUiState.Success(
                qanda.map { it.mapToQuestionAndResponseUi(selected) }.toImmutableList()
            ) as QAndAUiState
        }
    ).catch {
        emit(QAndAUiState.Failure(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = QAndAUiState.Loading(
            persistentListOf(QuestionAndResponseUi.fake, QuestionAndResponseUi.fake)
        )
    )

    fun expanded(qanda: QuestionAndResponseUi) {
        _selected.update { if (it == qanda) null else qanda }
    }
}
