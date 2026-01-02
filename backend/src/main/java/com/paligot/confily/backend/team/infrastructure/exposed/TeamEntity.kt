package com.paligot.confily.backend.team.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class TeamEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TeamEntity>(TeamTable) {
        fun findByGroupId(eventId: UUID, teamGroupId: UUID): SizedIterable<TeamEntity> = this
            .find { (TeamTable.eventId eq eventId) and (TeamTable.teamGroupId eq teamGroupId) }

        fun findByExternalId(eventId: UUID, externalId: String, provider: IntegrationProvider): TeamEntity? = this
            .find {
                val eventOp = TeamTable.eventId eq eventId
                val externalIdOp = TeamTable.externalId eq externalId
                val providerOp = TeamTable.externalProvider eq provider
                eventOp and externalIdOp and providerOp
            }
            .firstOrNull()
    }

    var eventId by TeamTable.eventId
    var event by EventEntity.Companion referencedOn TeamTable.eventId
    var name by TeamTable.name
    var role by TeamTable.role
    var bio by TeamTable.bio
    var photoUrl by TeamTable.photoUrl
    var teamGroupId by TeamTable.teamGroupId
    var teamGroup by TeamGroupEntity referencedOn TeamTable.teamGroupId
    var displayOrder by TeamTable.displayOrder
    var externalId by TeamTable.externalId
    var externalProvider by TeamTable.externalProvider
    var createdAt by TeamTable.createdAt
    var updatedAt by TeamTable.updatedAt
}
