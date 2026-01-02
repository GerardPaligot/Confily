package com.paligot.confily.backend.team.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class TeamGroupEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<TeamGroupEntity>(TeamGroupsTable) {
        fun findByEvent(eventId: UUID): SizedIterable<TeamGroupEntity> = this
            .find { TeamGroupsTable.eventId eq eventId }

        fun findByName(eventId: UUID, name: String): TeamGroupEntity? = this
            .find { (TeamGroupsTable.eventId eq eventId) and (TeamGroupsTable.name eq name) }
            .firstOrNull()
    }

    var eventId by TeamGroupsTable.eventId
    var event by EventEntity.Companion referencedOn TeamGroupsTable.eventId
    var name by TeamGroupsTable.name
    var displayOrder by TeamGroupsTable.displayOrder
    var createdAt by TeamGroupsTable.createdAt
}
