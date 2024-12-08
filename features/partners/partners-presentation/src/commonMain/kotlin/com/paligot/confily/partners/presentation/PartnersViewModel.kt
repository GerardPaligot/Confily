package com.paligot.confily.partners.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.partners.PartnerRepository
import com.paligot.confily.core.partners.entities.mapToPartnersActivitiesUi
import com.paligot.confily.models.ui.PartnersActivitiesUi
import com.paligot.confily.navigation.TabActions
import com.paligot.confily.style.theme.actions.TabAction
import com.paligot.confily.style.theme.actions.TabActionsUi
import kotlinx.collections.immutable.toImmutableList
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
                uiModel = it.mapToPartnersActivitiesUi(),
                tabActionsUi = TabActionsUi(
                    actions = mutableListOf<TabAction>().apply {
                        add(TabActions.partners)
                        if (it.activities.isNotEmpty()) {
                            add(TabActions.activities)
                        }
                    }.toImmutableList()
                )
            ) as PartnersUiState
        }
        .catch {
            it.printStackTrace()
            emit(PartnersUiState.Failure(it))
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = PartnersUiState.Loading(PartnersActivitiesUi.fake),
            started = SharingStarted.WhileSubscribed()
        )
}
