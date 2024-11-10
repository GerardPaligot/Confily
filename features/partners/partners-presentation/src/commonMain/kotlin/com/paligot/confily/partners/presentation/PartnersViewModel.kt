package com.paligot.confily.partners.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.partners.PartnerRepository
import com.paligot.confily.models.ui.PartnerGroupsUi
import com.paligot.confily.models.ui.PartnersActivitiesUi
import com.paligot.confily.navigation.TabActions
import com.paligot.confily.style.theme.actions.TabActionsUi
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class PartnersUiState {
    data class Loading(val uiModel: PartnersActivitiesUi) : PartnersUiState()
    data class Success(
        val uiModel: PartnersActivitiesUi,
        val tabActionsUi: TabActionsUi
    ) : PartnersUiState()

    data class Failure(val throwable: Throwable) : PartnersUiState()
}

class PartnersViewModel(repository: PartnerRepository) : ViewModel() {
    val uiState: StateFlow<PartnersUiState> = repository.partners()
        .map {
            PartnersUiState.Success(
                uiModel = it.mapToUi(),
                tabActionsUi = TabActionsUi(
                    actions = persistentListOf(
                        TabActions.partners
                    )
                )
            ) as PartnersUiState
        }
        .catch { emit(PartnersUiState.Failure(it)) }
        .stateIn(
            scope = viewModelScope,
            initialValue = PartnersUiState.Loading(PartnersActivitiesUi.fake),
            started = SharingStarted.WhileSubscribed()
        )
}

private fun PartnerGroupsUi.mapToUi(): PartnersActivitiesUi = PartnersActivitiesUi(
    partners = this,
    activities = persistentMapOf()
)
