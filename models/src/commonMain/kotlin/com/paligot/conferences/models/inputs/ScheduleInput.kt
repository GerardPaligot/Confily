package com.paligot.conferences.models.inputs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleInput(
  val time: String,
  val room: String,
  @SerialName("talk_id")
  val talkId: String? = null
): Validator {
  override fun validate(): List<String> {
    val errors = arrayListOf<String>()
    val timePattern = "[0-9]{2}:[0-9]{2}"
    if (!time.matches(Regex(timePattern))) errors.add("Your time should be formatted with this regex $timePattern")
    return errors
  }
}