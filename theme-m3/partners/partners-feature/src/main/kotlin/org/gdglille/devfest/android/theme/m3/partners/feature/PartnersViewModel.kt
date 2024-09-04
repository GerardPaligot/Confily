package org.gdglille.devfest.android.theme.m3.partners.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.paligot.confily.core.repositories.AgendaRepository
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

class PartnersViewModel(repository: AgendaRepository) : ViewModel() {
    val uiState: StateFlow<PartnersUiState> = repository.partners()
        .map { PartnersUiState.Success(it) }
        .catch {
            Firebase.crashlytics.recordException(it)
            PartnersUiState.Failure(it)
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = PartnersUiState.Loading(PartnerGroupsUi.fake),
            started = SharingStarted.WhileSubscribed()
        )
}
