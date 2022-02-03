package com.paligot.conferences.backend.schedulers

import com.paligot.conferences.models.ScheduleItem
import com.paligot.conferences.models.Talk
import com.paligot.conferences.models.inputs.ScheduleInput

fun ScheduleDb.convertToModel(talk: Talk?) = ScheduleItem(
  id = this.id,
  time = this.time,
  room = this.room,
  talk = talk
)

fun ScheduleInput.convertToDb(talkId: String? = null) = ScheduleDb(
  time = this.time,
  room = this.room,
  talkId = talkId
)
