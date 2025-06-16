package com.paligot.confily.backend.team.domain

import com.paligot.confily.models.TeamMember

interface TeamRepository {
    suspend fun list(eventId: String): Map<String, List<TeamMember>>
}
