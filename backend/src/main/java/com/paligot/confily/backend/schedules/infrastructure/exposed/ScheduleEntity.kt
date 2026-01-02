package com.paligot.confily.backend.schedules.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionTrackEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionEntity
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.and
import java.util.UUID

class ScheduleEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ScheduleEntity>(SchedulesTable) {
        fun findByEvent(eventId: UUID): SizedIterable<ScheduleEntity> = this
            .find { SchedulesTable.eventId eq eventId }

        fun findWithSession(eventId: UUID): SizedIterable<ScheduleEntity> = this
            .find { (SchedulesTable.eventId eq eventId) and (SchedulesTable.sessionId neq null) }

        fun findById(eventId: UUID, scheduleId: UUID): ScheduleEntity? = this
            .find { (SchedulesTable.eventId eq eventId) and (SchedulesTable.id eq scheduleId) }
            .firstOrNull()

        fun findByExternalId(eventId: UUID, externalId: String, provider: IntegrationProvider): ScheduleEntity? = this
            .find {
                val eventOp = SchedulesTable.eventId eq eventId
                val externalIdOp = SchedulesTable.externalId eq externalId
                val providerOp = SchedulesTable.externalProvider eq provider
                eventOp and externalIdOp and providerOp
            }
            .firstOrNull()
    }

    var eventId by SchedulesTable.eventId
    var event by EventEntity referencedOn SchedulesTable.eventId
    var sessionId by SchedulesTable.sessionId
    var session by SessionEntity optionalReferencedOn SchedulesTable.sessionId
    var eventSessionId by SchedulesTable.eventSessionId
    var eventSession by EventSessionEntity optionalReferencedOn SchedulesTable.eventSessionId
    var eventSessionTrackId by SchedulesTable.eventSessionTrackId
    var eventSessionTrack by EventSessionTrackEntity referencedOn SchedulesTable.eventSessionTrackId
    var displayOrder by SchedulesTable.displayOrder
    var startTime by SchedulesTable.startTime
    var endTime by SchedulesTable.endTime
    var externalId by SchedulesTable.externalId
    var externalProvider by SchedulesTable.externalProvider
    var createdAt by SchedulesTable.createdAt
    var updatedAt by SchedulesTable.updatedAt
}
