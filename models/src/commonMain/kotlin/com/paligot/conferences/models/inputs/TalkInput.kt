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
  val language: String = "",
  @SerialName("speaker_ids")
  val speakerIds: List<String> = emptyList()
): Validator {
  override fun validate(): List<String> = emptyList()
}