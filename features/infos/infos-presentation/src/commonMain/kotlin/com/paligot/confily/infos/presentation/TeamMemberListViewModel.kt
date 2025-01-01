package com.paligot.confily.infos.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paligot.confily.core.events.EventRepository
import com.paligot.confily.core.events.entities.mapToTeamMemberItemUi
import com.paligot.confily.models.ui.TeamMemberItemUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class TeamMemberListUiState {
    data class Loading(val members: ImmutableList<TeamMemberItemUi>) : TeamMemberListUiState()
    data class Success(val members: ImmutableList<TeamMemberItemUi>) : TeamMemberListUiState()
    data class Failure(val throwable: Throwable) : TeamMemberListUiState()
}

class TeamMemberListViewModel(repository: EventRepository) : ViewModel() {
    val uiState: StateFlow<TeamMemberListUiState> = repository.teamMembers()
        .map {
            TeamMemberListUiState.Success(
                it.map { it.mapToTeamMemberItemUi() }.toImmutableList()
            ) as TeamMemberListUiState
        }
        .catch { emit(TeamMemberListUiState.Failure(it)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = TeamMemberListUiState.Loading(persistentListOf(TeamMemberItemUi.fake))
        )
}
