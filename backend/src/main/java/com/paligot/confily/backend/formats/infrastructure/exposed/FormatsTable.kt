package com.paligot.confily.backend.formats.infrastructure.exposed

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

object FormatsTable : UUIDTable("formats") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE).index()
    val name = varchar("name", 255)
    val externalId = varchar("external_id", 255).nullable()
    val externalProvider = enumeration<IntegrationProvider>("external_provider").nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        uniqueIndex(eventId, name)
        uniqueIndex(eventId, externalId, externalProvider)
    }

    fun deleteDiff(
        eventId: UUID,
        externalIds: List<String>,
        provider: IntegrationProvider
    ) {
        deleteWhere {
            val eventOp = FormatsTable.eventId eq eventId
            val externalIdsOp = FormatsTable.externalId notInList externalIds
            val providerOp = FormatsTable.externalProvider eq provider
            eventOp and externalIdsOp and providerOp
        }
    }
}
