package com.paligot.conferences.backend.talks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class TalkDb(
  val id: String = "",
  val title: String = "",
  val level: String? = null,
  val abstract: String = "",
  val category: String = "",
  val format: String = "",
  val speakerIds: List<String> = emptyList(),
  val openFeedback: String? = null
)

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
)
