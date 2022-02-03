package com.paligot.conferences.backend.speakers

data class SpeakerDb(
  val id: String = "",
  val displayName: String = "",
  val bio: String = "",
  val company: String? = null,
  val photoUrl: String = "",
  val twitter: String? = null,
  val github: String? = null
)
