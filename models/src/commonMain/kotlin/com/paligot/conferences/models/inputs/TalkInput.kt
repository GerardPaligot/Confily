package com.paligot.conferences.models.inputs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TalkInput(
  val title: String = "",
  val level: String? = null,
  val abstract: String = "",
  val category: String = "",
  val format: String = "",
  @SerialName("speaker_ids")
  val speakerIds: List<String> = emptyList(),
  @SerialName("open_feedback")
  val openFeedback: String? = null
): Validator {
  override fun validate(): List<String> {
    val errors = arrayListOf<String>()
    if (speakerIds.isEmpty()) errors.add("You can't create a talk without speakers")
    if (openFeedback?.contains("openfeedback.io") == false) errors.add("Your openfeedback url is malformed")
    return errors
  }
}