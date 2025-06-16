package com.paligot.confily.backend.team.application

import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.TeamFirestore
import com.paligot.confily.backend.team.domain.TeamRepository
import com.paligot.confily.models.TeamMember

class TeamRepositoryDefault(
    private val eventDao: EventFirestore,
    private val teamFirestore: TeamFirestore
): TeamRepository {
    override suspend fun list(eventId: String): Map<String, List<TeamMember>> {
        val eventDb = eventDao.get(eventId)
        val orderMap = eventDb.teamGroups.associate { it.name to it.order }
        return teamFirestore.getAll(eventId)
            .sortedBy { orderMap[it.teamName] ?: 0 }
            .groupBy { it.teamName }
            .filter { it.key != "" }
            .map { entry -> entry.key to entry.value.map { it.convertToModel() } }
            .associate { it }
    }
}
