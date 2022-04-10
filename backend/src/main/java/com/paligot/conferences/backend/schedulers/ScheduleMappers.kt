package com.paligot.conferences.backend.schedulers

import com.paligot.conferences.backend.date.FormatterPattern
import com.paligot.conferences.backend.date.format
import com.paligot.conferences.models.ScheduleItem
import com.paligot.conferences.models.Talk
import com.paligot.conferences.models.inputs.ScheduleInput
import java.time.LocalDateTime

fun ScheduleDb.convertToModel(talk: Talk?) = ScheduleItem(
  id = this.id,
  time = LocalDateTime.parse(this.startTime).format(FormatterPattern.HoursMinutes),
  startTime = this.startTime,
  endTime = this.endTime,
  room = this.room,
  talk = talk
)

fun ScheduleInput.convertToDb(endTime: String, talkId: String? = null) = ScheduleDb(
  startTime = this.startTime,
  endTime = endTime,
  room = this.room,
  talkId = talkId
)
