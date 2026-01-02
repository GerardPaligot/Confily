package com.paligot.confily.backend.schedules.infrastructure.exposed

import com.paligot.confily.models.ScheduleItemV4
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun ScheduleEntity.toModel(): ScheduleItemV4 = ScheduleItemV4(
    id = this.id.value.toString(),
    order = this.displayOrder ?: 0,
    date = this.startTime.toLocalDateTime(TimeZone.UTC).date.toString(),
    startTime = this.startTime.toString(),
    endTime = this.endTime.toString(),
    room = this.eventSessionTrack.trackName,
    sessionId = this.session?.id?.value.toString()
)
