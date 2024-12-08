package com.paligot.confily.partners.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.partners.PartnerRepository
import com.paligot.confily.core.partners.entities.mapToPartnerUi
import com.paligot.confily.models.ui.PartnerUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class PartnerUiState {
    data class Loading(val partner: PartnerUi) : PartnerUiState()
    data class Success(val partner: PartnerUi) : PartnerUiState()
    data class Failure(val throwable: Throwable) : PartnerUiState()
}

class PartnerDetailViewModel(partnerId: String, repository: PartnerRepository) : ViewModel() {
    val uiState: StateFlow<PartnerUiState> = repository.partner(partnerId)
        .map { PartnerUiState.Success(it.mapToPartnerUi()) as PartnerUiState }
        .catch { emit(PartnerUiState.Failure(it)) }
        .stateIn(
            scope = viewModelScope,
            initialValue = PartnerUiState.Loading(PartnerUi.fake),
            started = SharingStarted.WhileSubscribed()
        )
}
