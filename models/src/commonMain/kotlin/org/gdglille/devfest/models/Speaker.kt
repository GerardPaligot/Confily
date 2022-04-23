package org.gdglille.devfest.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Speaker(
  val id: String,
  @SerialName("display_name")
  val displayName: String,
  val bio: String,
  val company: String?,
  @SerialName("photo_url")
  val photoUrl: String,
  val twitter: String?,
  val github: String?
)
