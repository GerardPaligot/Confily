package com.paligot.confily.schedules.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.schedules.SessionRepository
import com.paligot.confily.core.schedules.entities.mapToUi
import com.paligot.confily.models.ui.FiltersUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class AgendaFiltersUiState {
    data object Loading : AgendaFiltersUiState()
    data class Success(val filters: FiltersUi) : AgendaFiltersUiState()
    data class Failure(val throwable: Throwable) : AgendaFiltersUiState()
}

class AgendaFiltersViewModel(
    private val repository: SessionRepository
) : ViewModel() {
    val uiState: StateFlow<AgendaFiltersUiState> = repository.filters()
        .map { result -> AgendaFiltersUiState.Success(result.mapToUi()) as AgendaFiltersUiState }
        .catch { emit(AgendaFiltersUiState.Failure(it)) }
        .stateIn(
            scope = viewModelScope,
            initialValue = AgendaFiltersUiState.Loading,
            started = SharingStarted.WhileSubscribed()
        )

    fun applyFavoriteFilter(selected: Boolean) = viewModelScope.launch {
        repository.applyFavoriteFilter(selected)
    }

    fun applyCategoryFilter(categoryId: String, selected: Boolean) = viewModelScope.launch {
        repository.applyCategoryFilter(categoryId, selected)
    }

    fun applyFormatFilter(formatId: String, selected: Boolean) = viewModelScope.launch {
        repository.applyFormatFilter(formatId, selected)
    }
}
