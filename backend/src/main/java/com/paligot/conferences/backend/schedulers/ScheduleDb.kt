package com.paligot.conferences.backend.schedulers

data class ScheduleDb(
  val time: String = "",
  val room: String = "",
  val talkId: String? = null
) {
  val id: String = talkId ?: "${time}-pause"
}
