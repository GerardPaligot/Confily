package com.paligot.conferences.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Talk(
  val id: String,
  val title: String,
  val level: String?,
  val abstract: String,
  val category: String,
  val format: String,
  val speakers: List<Speaker>,
  @SerialName("open_feedback")
  val openFeedback: String?
)
