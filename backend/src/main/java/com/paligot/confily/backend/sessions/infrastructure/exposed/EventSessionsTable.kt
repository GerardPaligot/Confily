package com.paligot.confily.backend.sessions.infrastructure.exposed

import com.paligot.confily.backend.addresses.infrastructure.exposed.AddressesTable
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

object EventSessionsTable : UUIDTable("event_sessions") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE).index()
    val title = varchar("title", 500)
    val description = text("description").nullable()
    val addressId = reference("address_id", AddressesTable, onDelete = ReferenceOption.SET_NULL).nullable()
    val externalId = varchar("external_id", 255).nullable()
    val externalProvider = enumeration<IntegrationProvider>("external_provider").nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        uniqueIndex(eventId, externalId, externalProvider)
    }

    fun deleteDiff(
        eventId: UUID,
        externalIds: List<String>,
        provider: IntegrationProvider
    ) {
        deleteWhere {
            val eventOp = EventSessionsTable.eventId eq eventId
            val externalIdsOp = EventSessionsTable.externalId notInList externalIds
            val providerOp = EventSessionsTable.externalProvider eq provider
            eventOp and externalIdsOp and providerOp
        }
    }
}
