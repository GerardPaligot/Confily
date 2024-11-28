package com.paligot.confily.wear.partners.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.partners.PartnerRepository
import com.paligot.confily.core.partners.entities.mapToUi
import com.paligot.confily.models.ui.PartnerGroupsUi
import com.paligot.confily.wear.partners.panes.PartnerModelUi
import com.paligot.confily.wear.partners.panes.PartnersModelUi
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class PartnersUiState {
    data object Loading : PartnersUiState()
    data class Success(val modelUi: PartnersModelUi) : PartnersUiState()
}

class PartnersViewModel(repository: PartnerRepository) : ViewModel() {
    val uiState = repository.partners()
        .map { PartnersUiState.Success(it.mapToUi().partners.toModelUi()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PartnersUiState.Loading
        )
}

private fun PartnerGroupsUi.toModelUi(): PartnersModelUi = PartnersModelUi(
    groups.associate { (type, partners) ->
        type to partners.map { partner ->
            PartnerModelUi(id = partner.id, name = partner.name, url = partner.logoUrl)
        }.toImmutableList()
    }.toImmutableMap()
)
