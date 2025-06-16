package com.paligot.confily.backend.team.domain

import com.paligot.confily.models.inputs.TeamMemberInput

interface TeamAdminRepository {
    suspend fun create(eventId: String, teamInput: TeamMemberInput): String
    suspend fun update(eventId: String, teamMemberId: String, input: TeamMemberInput): String
}
