package com.paligot.confily.infos.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.events.entities.mapToTeamMemberUi
import com.paligot.confily.infos.routes.TeamMember
import com.paligot.confily.models.ui.TeamMemberUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class TeamMemberUiState {
    data class Loading(val member: TeamMemberUi) : TeamMemberUiState()
    data class Success(val member: TeamMemberUi) : TeamMemberUiState()
    data class Failure(val throwable: Throwable) : TeamMemberUiState()
}

class TeamMemberViewModel(
    stateHandle: SavedStateHandle,
    repository: EventRepository
) : ViewModel() {
    private val route = stateHandle.toRoute<TeamMember>()

    val uiState: StateFlow<TeamMemberUiState> = repository.teamMember(route.id)
        .map {
            if (it != null) {
                TeamMemberUiState.Success(it.mapToTeamMemberUi()) as TeamMemberUiState
            } else {
                throw NullPointerException("Team member not found")
            }
        }
        .catch {
            it.printStackTrace()
            emit(TeamMemberUiState.Failure(it))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = TeamMemberUiState.Loading(TeamMemberUi.fake)
        )
}
