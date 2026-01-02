package com.paligot.confily.backend.schedules.application

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.schedules.domain.ScheduleAdminRepository
import com.paligot.confily.backend.schedules.infrastructure.exposed.ScheduleEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionTrackEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionEntity
import com.paligot.confily.models.inputs.ScheduleInput
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class ScheduleAdminRepositoryExposed(private val database: Database) : ScheduleAdminRepository {

    override suspend fun create(eventId: String, scheduleInput: ScheduleInput): String = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        val event = EventEntity[eventUuid]

        // Find or create event session track for the room
        val track = EventSessionTrackEntity
            .findByTrackName(eventUuid, scheduleInput.room)
            ?: EventSessionTrackEntity.new {
                this.event = event
                this.trackName = scheduleInput.room
                this.displayOrder = 0
            }

        // Parse timestamps
        val startTime = Instant.parse(scheduleInput.startTime)
        val endTime = scheduleInput.endTime?.let { Instant.parse(it) } ?: startTime

        // Find session if talkId is provided
        val session = scheduleInput.talkId?.let {
            SessionEntity.findById(UUID.fromString(it))
        }

        // Create schedule
        val schedule = ScheduleEntity.new {
            this.event = event
            this.sessionId = session?.id
            this.eventSessionId = null
            this.eventSessionTrack = track
            this.displayOrder = scheduleInput.order
            this.startTime = startTime
            this.endTime = endTime
        }

        schedule.id.value.toString()
    }

    override suspend fun delete(eventId: String, scheduleId: String) {
        val eventUuid = UUID.fromString(eventId)
        val scheduleUuid = UUID.fromString(scheduleId)
        transaction(db = database) {
            ScheduleEntity
                .findById(eventUuid, scheduleUuid)
                ?.delete()
        }
    }
}
