package com.paligot.confily.backend.schedules.infrastructure.firestore

import com.paligot.confily.backend.internals.helpers.date.FormatterPattern
import com.paligot.confily.backend.internals.helpers.date.format
import com.paligot.confily.models.ScheduleItem
import com.paligot.confily.models.ScheduleItemV4
import com.paligot.confily.models.Session
import com.paligot.confily.models.Talk
import com.paligot.confily.models.inputs.ScheduleInput
import java.time.LocalDateTime

fun ScheduleEntity.convertToModel(talk: Talk?) = ScheduleItem(
    id = this.id,
    order = order ?: 0,
    time = LocalDateTime.parse(this.startTime).format(FormatterPattern.HoursMinutes),
    startTime = this.startTime,
    endTime = this.endTime,
    room = this.room,
    talk = talk
)

fun ScheduleEntity.convertToModelV4() = ScheduleItemV4(
    id = this.id,
    order = order ?: 0,
    date = LocalDateTime.parse(this.startTime).format(FormatterPattern.YearMonthDay),
    startTime = this.startTime,
    endTime = this.endTime,
    room = this.room,
    sessionId = talkId ?: this.id
)

fun ScheduleItemV4.convertToEventSession(): Session = Session.Event(
    id = id,
    title = "Break",
    description = null,
    address = null
)

fun ScheduleInput.convertToEntity(endTime: String, talkId: String? = null) = ScheduleEntity(
    order = order,
    startTime = this.startTime,
    endTime = endTime,
    room = this.room,
    talkId = talkId
)
