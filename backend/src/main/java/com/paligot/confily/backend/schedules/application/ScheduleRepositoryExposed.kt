package com.paligot.confily.backend.schedules.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.schedules.domain.ScheduleRepository
import com.paligot.confily.backend.schedules.infrastructure.exposed.ScheduleEntity
import com.paligot.confily.backend.sessions.infrastructure.exposed.toModel
import com.paligot.confily.models.ScheduleItem
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import kotlin.time.DurationUnit

class ScheduleRepositoryExposed(private val database: Database) : ScheduleRepository {
    override suspend fun get(eventId: String, scheduleId: String): ScheduleItem = transaction(db = database) {
        val eventUuid = UUID.fromString(eventId)
        val scheduleUuid = UUID.fromString(scheduleId)
        val schedule = ScheduleEntity
            .findById(eventUuid, scheduleUuid)
            ?: throw NotFoundException("Schedule item $scheduleId not found for event $eventId")
        val session = schedule.session
        ScheduleItem(
            id = schedule.id.value.toString(),
            order = schedule.displayOrder ?: 0,
            time = (schedule.startTime - schedule.endTime).toString(unit = DurationUnit.HOURS),
            startTime = schedule.startTime.toLocalDateTime(TimeZone.UTC).toString(),
            endTime = schedule.endTime.toLocalDateTime(TimeZone.UTC).toString(),
            room = schedule.eventSessionTrack.trackName,
            talk = session?.toModel()
        )
    }
}
