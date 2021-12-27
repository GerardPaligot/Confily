package com.paligot.conferences.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Partner(
  val id: String,
  val name: String,
  val url: String,
  @SerialName("picture_url")
  val pictureUrl: String,
  val type: String
)
