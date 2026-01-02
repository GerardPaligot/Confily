package com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.schedules.infrastructure.exposed.ScheduleEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.EventSessionTrackEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.SessionEntity
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.SessionOP
import kotlinx.datetime.Instant

fun SessionOP.toScheduleEntity(event: EventEntity): ScheduleEntity = ScheduleEntity
    .findByExternalId(event.id.value, this@toScheduleEntity.id)
    ?.let { entity ->
        entity.startTime = Instant.parse(this@toScheduleEntity.dateStart!!)
        entity.endTime = Instant.parse(this@toScheduleEntity.dateEnd!!)
        entity
    }
    ?: ScheduleEntity.new {
        this.event = event
        if (this@toScheduleEntity.speakerIds.isEmpty()) {
            this.eventSession = EventSessionEntity
                .findByExternalId(event.id.value, this@toScheduleEntity.id)
        } else {
            this.session = SessionEntity
                .findByExternalId(event.id.value, this@toScheduleEntity.id)
        }
        val trackId = this@toScheduleEntity.trackId
            ?: throw NotFoundException("Track ID is required for schedule item ${this@toScheduleEntity.id}")
        this.eventSessionTrack = EventSessionTrackEntity
            .findByExternalId(event.id.value, trackId)
            ?: throw NotFoundException("Track with external ID $trackId not found for event ${event.id.value}")
        // this.displayOrder = this@toScheduleEntity
        this.startTime = Instant.parse(this@toScheduleEntity.dateStart!!)
        this.endTime = Instant.parse(this@toScheduleEntity.dateEnd!!)
        this.externalId = this@toScheduleEntity.id
    }
