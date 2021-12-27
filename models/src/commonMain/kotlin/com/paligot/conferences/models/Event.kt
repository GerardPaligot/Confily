package com.paligot.conferences.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventAddress(
  val address: String,
  val country: String,
  @SerialName("country_code")
  val countryCode: String,
  val city: String,
  val lat: Double,
  val lng: Double
)

@Serializable
data class Event(
  val id: String,
  val name: String,
  val address: EventAddress,
  @SerialName("start_date")
  val startDate: String,
  @SerialName("end_date")
  val endDate: String
)
