package com.paligot.confily.backend.schedules.infrastructure.exposed

import com.paligot.confily.backend.ForbiddenException
import com.paligot.confily.backend.sessions.infrastructure.exposed.toModel
import com.paligot.confily.models.Info
import com.paligot.confily.models.PlanningItem
import com.paligot.confily.models.ScheduleItem
import com.paligot.confily.models.ScheduleItemV3
import com.paligot.confily.models.ScheduleItemV4
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.DurationUnit

fun ScheduleEntity.toModel(): ScheduleItem = ScheduleItem(
    id = this.id.value.toString(),
    order = this.displayOrder ?: 0,
    startTime = this.startTime.toLocalDateTime(TimeZone.UTC).toString(),
    endTime = this.endTime.toLocalDateTime(TimeZone.UTC).toString(),
    room = this.eventSessionTrack.trackName,
    time = (startTime - endTime).toString(unit = DurationUnit.MINUTES),
    talk = this.session?.toModel()
)

fun ScheduleEntity.toPlanningItemModel(): PlanningItem {
    if (session != null) {
        return PlanningItem.TalkItem(
            id = session!!.id.value.toString(),
            order = this.displayOrder ?: 0,
            startTime = this.startTime.toLocalDateTime(TimeZone.UTC).toString(),
            endTime = this.endTime.toLocalDateTime(TimeZone.UTC).toString(),
            room = this.eventSessionTrack.trackName,
            talk = session!!.toModel()
        )
    } else if (eventSession != null) {
        return PlanningItem.EventItem(
            id = this.id.value.toString(),
            order = this.displayOrder ?: 0,
            startTime = this.startTime.toLocalDateTime(TimeZone.UTC).toString(),
            endTime = this.endTime.toLocalDateTime(TimeZone.UTC).toString(),
            room = this.eventSessionTrack.trackName,
            info = Info(
                id = eventSession!!.id.value.toString(),
                title = eventSession!!.title,
                description = eventSession!!.description
            )
        )
    } else {
        throw ForbiddenException("Schedule ${this.id.value} has neither session nor event session")
    }
}

fun ScheduleEntity.toModelV3(): ScheduleItemV3 = ScheduleItemV3(
    id = this.id.value.toString(),
    order = this.displayOrder ?: 0,
    date = this.startTime.toLocalDateTime(TimeZone.UTC).date.toString(),
    startTime = this.startTime.toLocalDateTime(TimeZone.UTC).toString(),
    endTime = this.endTime.toLocalDateTime(TimeZone.UTC).toString(),
    room = this.eventSessionTrack.trackName,
    talkId = this.session?.id?.value.toString()
)

fun ScheduleEntity.toModelV4(): ScheduleItemV4 = ScheduleItemV4(
    id = this.id.value.toString(),
    order = this.displayOrder ?: 0,
    date = this.startTime.toLocalDateTime(TimeZone.UTC).date.toString(),
    startTime = this.startTime.toLocalDateTime(TimeZone.UTC).toString(),
    endTime = this.endTime.toLocalDateTime(TimeZone.UTC).toString(),
    room = this.eventSessionTrack.trackName,
    sessionId = this.session?.id?.value.toString()
)
