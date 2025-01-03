package com.paligot.confily.wear.events.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.events.entities.CodeOfConduct
import com.paligot.confily.infos.ui.models.CoCUi
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
        .map { CoCUiState.Success(it.mapToCoCUi()) }
        .catch { CoCUiState.Failure }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CoCUiState.Loading
        )
}

internal fun CodeOfConduct.mapToCoCUi(): CoCUi = CoCUi(
    text = content!!,
    phone = phone,
    email = email
)
