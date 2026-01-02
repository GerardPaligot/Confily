package com.paligot.confily.backend.team.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventsTable
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.notInList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.UUID

object TeamTable : UUIDTable("team") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 255)
    val role = varchar("role", 255).nullable()
    val bio = text("bio").nullable()
    val photoUrl = text("photo_url").nullable()
    val teamGroupId = reference("team_group_id", TeamGroupsTable, onDelete = ReferenceOption.CASCADE)
    val displayOrder = integer("display_order").default(0)
    val externalId = varchar("external_id", 255).nullable()
    val externalProvider = enumeration<IntegrationProvider>("external_provider").nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, eventId)
        index(isUnique = false, teamGroupId)
        index(isUnique = false, eventId, displayOrder)
        uniqueIndex(eventId, externalId, externalProvider)
    }

    fun deleteDiff(
        eventId: UUID,
        externalIds: List<String>,
        provider: IntegrationProvider
    ) {
        deleteWhere {
            val eventOp = TeamTable.eventId eq eventId
            val externalIdsOp = TeamTable.externalId notInList externalIds
            val providerOp = TeamTable.externalProvider eq provider
            eventOp and externalIdsOp and providerOp
        }
    }
}
