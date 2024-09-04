package com.paligot.confily.partners.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.paligot.confily.core.repositories.AgendaRepository
import com.paligot.confily.models.ui.PartnerItemUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class PartnerUiState {
    data class Loading(val partner: PartnerItemUi) : PartnerUiState()
    data class Success(val partner: PartnerItemUi) : PartnerUiState()
    data class Failure(val throwable: Throwable) : PartnerUiState()
}

class PartnerDetailViewModel(partnerId: String, repository: AgendaRepository) : ViewModel() {
    val uiState: StateFlow<PartnerUiState> = repository.partner(partnerId)
        .map { PartnerUiState.Success(it) }
        .catch {
            Firebase.crashlytics.recordException(it)
            PartnerUiState.Failure(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = PartnerUiState.Loading(PartnerItemUi.fake),
            started = SharingStarted.WhileSubscribed()
        )
}
