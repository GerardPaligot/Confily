package com.paligot.confily.backend.schedules.infrastructure.exposed

import com.paligot.confily.backend.sessions.infrastructure.exposed.toModel
import com.paligot.confily.models.ScheduleItem
import com.paligot.confily.models.ScheduleItemV3
import com.paligot.confily.models.ScheduleItemV4
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.DurationUnit

fun ScheduleEntity.toModel(): ScheduleItem {
    val timezone = TimeZone.of(this.event.timezone)
    return ScheduleItem(
        id = this.id.value.toString(),
        order = this.displayOrder ?: 0,
        startTime = this.startTime.toLocalDateTime(timezone).toString(),
        endTime = this.endTime.toLocalDateTime(timezone).toString(),
        room = this.eventSessionTrack.trackName,
        time = (startTime - endTime).toString(unit = DurationUnit.MINUTES),
        talk = this.session?.toModel()
    )
}

fun ScheduleEntity.toModelV3(): ScheduleItemV3 {
    val timezone = TimeZone.of(this.event.timezone)
    return ScheduleItemV3(
        id = this.id.value.toString(),
        order = this.displayOrder ?: 0,
        date = this.startTime.toLocalDateTime(timezone).date.toString(),
        startTime = this.startTime.toLocalDateTime(timezone).toString(),
        endTime = this.endTime.toLocalDateTime(timezone).toString(),
        room = this.eventSessionTrack.trackName,
        talkId = this.session?.id?.value.toString()
    )
}

fun ScheduleEntity.toModelV4(): ScheduleItemV4 {
    val timezone = TimeZone.of(this.event.timezone)
    return ScheduleItemV4(
        id = this.id.value.toString(),
        order = this.displayOrder ?: 0,
        date = this.startTime.toLocalDateTime(timezone).date.toString(),
        startTime = this.startTime.toLocalDateTime(timezone).toString(),
        endTime = this.endTime.toLocalDateTime(timezone).toString(),
        room = this.eventSessionTrack.trackName,
        sessionId = this.session?.id?.value.toString()
    )
}
