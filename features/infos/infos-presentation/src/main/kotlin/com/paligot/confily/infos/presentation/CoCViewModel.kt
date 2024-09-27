package com.paligot.confily.infos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.models.ui.CoCUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class CoCUiState {
    data object Loading : CoCUiState()
    data class Success(val coc: CoCUi) : CoCUiState()
    data class Failure(val throwable: Throwable) : CoCUiState()
}

class CoCViewModel(repository: EventRepository) : ViewModel() {
    val uiState: StateFlow<CoCUiState> = repository.coc()
        .map { CoCUiState.Success(it) }
        .catch {
            Firebase.crashlytics.recordException(it)
            CoCUiState.Failure(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CoCUiState.Loading
        )
}
