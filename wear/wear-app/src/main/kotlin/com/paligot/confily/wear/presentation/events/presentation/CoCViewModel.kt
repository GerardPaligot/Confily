package com.paligot.confily.wear.presentation.events.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.models.ui.CoCUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class CoCUiState {
    data object Loading : CoCUiState()
    data object Failure : CoCUiState()
    data class Success(val modelUi: CoCUi) : CoCUiState()
}

class CoCViewModel(repository: EventRepository) : ViewModel() {
    val uiState = repository.coc()
        .map { CoCUiState.Success(it) }
        .catch { CoCUiState.Failure }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CoCUiState.Loading
        )
}
