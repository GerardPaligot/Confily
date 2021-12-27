package com.paligot.conferences.models

import kotlinx.serialization.Serializable

@Serializable
data class Agenda(
  val talks: Map<String, List<ScheduleItem>>
)
