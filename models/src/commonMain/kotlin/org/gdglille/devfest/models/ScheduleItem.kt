package org.gdglille.devfest.models

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleItem(
  val id: String,
  @Deprecated("Replaced by startTime property.")
  val time: String,
  val startTime: String,
  val endTime: String,
  val room: String,
  val talk: Talk?
)
