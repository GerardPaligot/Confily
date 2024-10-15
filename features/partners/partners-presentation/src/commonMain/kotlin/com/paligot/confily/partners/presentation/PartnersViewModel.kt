package com.paligot.confily.partners.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.partners.PartnerRepository
import com.paligot.confily.models.ui.PartnerGroupsUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class PartnersUiState {
    data class Loading(val partners: PartnerGroupsUi) : PartnersUiState()
    data class Success(val partners: PartnerGroupsUi) : PartnersUiState()
    data class Failure(val throwable: Throwable) : PartnersUiState()
}

class PartnersViewModel(repository: PartnerRepository) : ViewModel() {
    val uiState: StateFlow<PartnersUiState> = repository.partners()
        .map { PartnersUiState.Success(it) as PartnersUiState }
        .catch { emit(PartnersUiState.Failure(it)) }
        .stateIn(
            scope = viewModelScope,
            initialValue = PartnersUiState.Loading(PartnerGroupsUi.fake),
            started = SharingStarted.WhileSubscribed()
        )
}
