package com.paligot.confily.infos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.events.entities.CodeOfConduct
import com.paligot.confily.infos.ui.models.CoCUi
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
        .map {
            if (it.content == null) {
                throw IllegalStateException("No CoC found")
            }
            CoCUiState.Success(it.mapToCoCUi()) as CoCUiState
        }
        .catch { emit(CoCUiState.Failure(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CoCUiState.Loading
        )
}

internal fun CodeOfConduct.mapToCoCUi(): CoCUi = CoCUi(
    text = content!!,
    phone = phone,
    email = email
)
