package org.gdglille.devfest.android.theme.m3.schedules.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.repositories.AgendaRepository
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.models.ui.FiltersUi
import com.paligot.confily.models.ui.FormatUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class AgendaFiltersUiState {
    data object Loading : AgendaFiltersUiState()
    data class Success(val filters: FiltersUi) : AgendaFiltersUiState()
    data class Failure(val throwable: Throwable) : AgendaFiltersUiState()
}

class AgendaFiltersViewModel(
    private val repository: AgendaRepository
) : ViewModel() {
    val uiState: StateFlow<AgendaFiltersUiState> = repository.filters()
        .map { result -> AgendaFiltersUiState.Success(result) }
        .stateIn(
            scope = viewModelScope,
            initialValue = AgendaFiltersUiState.Loading,
            started = SharingStarted.WhileSubscribed()
        )

    fun applyFavoriteFilter(selected: Boolean) = viewModelScope.launch {
        repository.applyFavoriteFilter(selected)
    }

    fun applyCategoryFilter(categoryUi: CategoryUi, selected: Boolean) = viewModelScope.launch {
        repository.applyCategoryFilter(categoryUi, selected)
    }

    fun applyFormatFilter(formatUi: FormatUi, selected: Boolean) = viewModelScope.launch {
        repository.applyFormatFilter(formatUi, selected)
    }
}
