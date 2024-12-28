package com.paligot.confily.core.events

import com.paligot.confily.core.combineAllSerializableScopedFlow
import com.paligot.confily.core.events.TeamMembersQueries.Scopes.TEAM_MEMBERS
import com.paligot.confily.core.getSerializableScopedFlow
import com.paligot.confily.core.putSerializableScoped
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.flow.Flow

class TeamMembersQueries(private val settings: ObservableSettings) {
    private object Scopes {
        const val TEAM_MEMBERS = "team-members"
    }

    fun insertTeamMember(teamMember: TeamMemberDb) {
        settings.putSerializableScoped(TEAM_MEMBERS, teamMember.id, teamMember)
    }

    fun selectTeamMembers(eventId: String): Flow<List<TeamMemberDb>> =
        settings.combineAllSerializableScopedFlow<TeamMemberDb>(TEAM_MEMBERS) { it.eventId == eventId }

    fun selectTeamMember(memberId: String): Flow<TeamMemberDb?> =
        settings.getSerializableScopedFlow(TEAM_MEMBERS, memberId)
}
