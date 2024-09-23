package com.paligot.confily.wear.presentation.partners.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.partners.PartnerRepository
import com.paligot.confily.models.ui.PartnerItemUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class PartnerUiState {
    data object Loading : PartnerUiState()
    data class Success(val modelUi: PartnerItemUi) : PartnerUiState()
}

class PartnerViewModel(partnerId: String, repository: PartnerRepository) : ViewModel() {
    val uiState = repository.partner(partnerId)
        .map { PartnerUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PartnerUiState.Loading
        )
}
