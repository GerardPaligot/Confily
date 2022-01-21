package com.paligot.conferences.models

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleItem(
  val id: String,
  val time: String,
  val room: String,
  val talk: Talk?
)
