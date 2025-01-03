package com.paligot.confily.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.events.entities.FeatureFlags
import com.paligot.confily.core.navigation.NavigationAction
import com.paligot.confily.core.navigation.NavigationActionsUi
import com.paligot.confily.core.navigation.NavigationBar
import com.paligot.confily.core.networking.UserRepository
import com.paligot.confily.core.networking.entities.UserItem
import com.paligot.confily.navigation.BottomActions
import com.paligot.confily.networking.ui.models.VCardModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class MainNavigationUiState {
    data object Loading : MainNavigationUiState()
    data class Success(val navActions: NavigationActionsUi) : MainNavigationUiState()
    data class Failure(val throwable: Throwable) : MainNavigationUiState()
}

class MainNavigationViewModel(
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val uiState: StateFlow<MainNavigationUiState> = eventRepository.featureFlags()
        .map { MainNavigationUiState.Success(navActions(it)) as MainNavigationUiState }
        .catch { emit(MainNavigationUiState.Failure(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MainNavigationUiState.Loading
        )

    fun saveTicket(barcode: String) = viewModelScope.launch {
        eventRepository.insertOrUpdateTicket(barcode)
    }

    fun saveNetworkingProfile(model: VCardModel) = viewModelScope.launch {
        userRepository.insertUserScanned(model.mapToEntity())
    }

    private fun navActions(features: FeatureFlags): NavigationActionsUi =
        NavigationActionsUi(
            actions = arrayListOf<NavigationAction<out NavigationBar>>().apply {
                add(BottomActions.agenda)
                add(BottomActions.speakers)
                if (features.hasNetworking) {
                    add(BottomActions.myProfile)
                }
                if (features.hasPartnerList) {
                    add(BottomActions.partners)
                }
                add(BottomActions.event)
            }.toImmutableList()
        )
}

private fun VCardModel.mapToEntity(): UserItem = UserItem(
    email = email,
    firstName = firstName,
    lastName = lastName,
    company = company
)
