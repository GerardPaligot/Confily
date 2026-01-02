package com.paligot.confily.backend.qanda.infrastructure.exposed

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

object QAndATable : UUIDTable("qanda") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE)
    val displayOrder = integer("display_order").default(0)
    val language = varchar("language", 10)
    val question = text("question")
    val response = text("response")
    val externalId = varchar("external_id", 255).nullable()
    val externalProvider = enumeration<IntegrationProvider>("external_provider").nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, eventId)
        index(isUnique = false, language)
        index(isUnique = false, eventId, displayOrder)
        uniqueIndex(eventId, externalId, externalProvider)
    }

    fun deleteDiff(
        eventId: java.util.UUID,
        externalIds: List<String>,
        provider: IntegrationProvider
    ) {
        deleteWhere {
            val eventOp = QAndATable.eventId eq eventId
            val externalIdsOp = QAndATable.externalId notInList externalIds
            val providerOp = QAndATable.externalProvider eq provider
            eventOp and externalIdsOp and providerOp
        }
    }
}
