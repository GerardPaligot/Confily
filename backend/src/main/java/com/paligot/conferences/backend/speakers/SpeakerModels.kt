package com.paligot.conferences.backend.speakers

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class SpeakerDb(
  val id: String = "",
  val displayName: String = "",
  val bio: String = "",
  val company: String? = null,
  val photoUrl: String = "",
  val twitter: String? = null,
  val github: String? = null
)

@Serializable
data class SpeakerInput(
  @SerialName("display_name")
  val displayName: String,
  val bio: String,
  val company: String? = null,
  @SerialName("photo_url")
  val photoUrl: String,
  val twitter: String? = null,
  val github: String? = null
)
