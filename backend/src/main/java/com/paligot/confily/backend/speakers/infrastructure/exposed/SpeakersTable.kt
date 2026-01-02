package com.paligot.confily.backend.speakers.infrastructure.exposed

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

object SpeakersTable : UUIDTable("speakers") {
    val eventId = reference("event_id", EventsTable, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 255)
    val bio = text("bio").nullable()
    val photoUrl = text("photo_url").nullable()
    val pronouns = varchar("pronouns", 50).nullable()
    val email = varchar("email", 255).nullable()
    val company = varchar("company", 255).nullable()
    val jobTitle = varchar("job_title", 255).nullable()
    val externalId = varchar("external_id", 255).nullable()
    val externalProvider = enumeration<IntegrationProvider>("external_provider").nullable()
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Clock.System.now() }

    init {
        index(isUnique = false, eventId)
        index(isUnique = false, eventId, name)
        index(isUnique = false, email)
        index(isUnique = false, company)
        uniqueIndex(eventId, externalId, externalProvider)
    }

    fun deleteDiff(
        eventId: UUID,
        externalIds: List<String>,
        provider: IntegrationProvider
    ) {
        deleteWhere {
            val eventOp = SpeakersTable.eventId eq eventId
            val externalIdsOp = SpeakersTable.externalId notInList externalIds
            val providerOp = SpeakersTable.externalProvider eq provider
            eventOp and externalIdsOp and providerOp
        }
    }
}
