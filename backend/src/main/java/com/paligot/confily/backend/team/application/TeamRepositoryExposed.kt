package com.paligot.confily.backend.team.application

import com.paligot.confily.backend.team.domain.TeamRepository
import com.paligot.confily.backend.team.infrastructure.exposed.TeamEntity
import com.paligot.confily.backend.team.infrastructure.exposed.TeamGroupEntity
import com.paligot.confily.backend.team.infrastructure.exposed.TeamGroupsTable
import com.paligot.confily.backend.team.infrastructure.exposed.TeamTable
import com.paligot.confily.backend.team.infrastructure.exposed.toModel
import com.paligot.confily.models.TeamMember
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class TeamRepositoryExposed(private val database: Database) : TeamRepository {
    override suspend fun list(eventId: String): Map<String, List<TeamMember>> = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        val groups = TeamGroupEntity
            .findByEvent(eventUuid)
            .orderBy(TeamGroupsTable.displayOrder to SortOrder.ASC)
        groups.associate { group ->
            val members = TeamEntity
                .findByGroupId(eventUuid, group.id.value)
                .orderBy(TeamTable.displayOrder to SortOrder.DESC)
                .map { it.toModel() }
            group.name to members
        }
    }
}
