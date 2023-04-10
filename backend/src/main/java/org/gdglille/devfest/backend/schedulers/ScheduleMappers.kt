package org.gdglille.devfest.backend.schedulers

import org.gdglille.devfest.backend.internals.date.FormatterPattern
import org.gdglille.devfest.backend.internals.date.format
import org.gdglille.devfest.models.ScheduleItem
import org.gdglille.devfest.models.Talk
import org.gdglille.devfest.models.inputs.ScheduleInput
import java.time.LocalDateTime

fun ScheduleDb.convertToModel(talk: Talk?) = ScheduleItem(
    id = this.id,
    order = order ?: 0,
    time = LocalDateTime.parse(this.startTime).format(FormatterPattern.HoursMinutes),
    startTime = this.startTime,
    endTime = this.endTime,
    room = this.room,
    talk = talk
)

fun ScheduleInput.convertToDb(endTime: String, talkId: String? = null) = ScheduleDb(
    order = order,
    startTime = this.startTime,
    endTime = endTime,
    room = this.room,
    talkId = talkId
)
