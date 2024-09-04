package org.gdglille.devfest.android.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.paligot.confily.core.repositories.AgendaRepository
import com.paligot.confily.core.repositories.UserRepository
import com.paligot.confily.models.ui.ScaffoldConfigUi
import com.paligot.confily.models.ui.UserNetworkingUi
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.gdglille.devfest.android.theme.m3.style.actions.NavigationAction
import org.gdglille.devfest.android.theme.m3.style.actions.NavigationActionsUi
import org.gdglille.devfest.theme.m3.navigation.BottomActions

sealed class MainNavigationUiState {
    data object Loading : MainNavigationUiState()
    data class Success(val navActions: NavigationActionsUi) : MainNavigationUiState()
    data class Failure(val throwable: Throwable) : MainNavigationUiState()
}

class MainNavigationViewModel(
    private val agendaRepository: AgendaRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val uiState: StateFlow<MainNavigationUiState> = agendaRepository.scaffoldConfig()
        .map { MainNavigationUiState.Success(navActions(it)) }
        .catch {
            Firebase.crashlytics.recordException(it)
            MainNavigationUiState.Failure(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MainNavigationUiState.Loading
        )

    fun saveTicket(barcode: String) = viewModelScope.launch {
        agendaRepository.insertOrUpdateTicket(barcode)
    }

    fun saveNetworkingProfile(user: UserNetworkingUi) = viewModelScope.launch {
        userRepository.insertNetworkingProfile(user)
    }

    private fun navActions(config: ScaffoldConfigUi): NavigationActionsUi = NavigationActionsUi(
        actions = arrayListOf<NavigationAction>().apply {
            add(BottomActions.agenda)
            add(BottomActions.speakers)
            if (config.hasNetworking) {
                add(BottomActions.myProfile)
            }
            if (config.hasPartnerList) {
                add(BottomActions.partners)
            }
            add(BottomActions.event)
        }.toImmutableList()
    )
}
