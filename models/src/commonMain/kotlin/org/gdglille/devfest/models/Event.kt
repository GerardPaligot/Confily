package org.gdglille.devfest.models

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
data class EventPartners(
  val golds: List<Partner>,
  val silvers: List<Partner>,
  val bronzes: List<Partner>,
  val others: List<Partner>
)

@Serializable
data class Event(
  val id: String,
  val name: String,
  val address: EventAddress,
  @SerialName("start_date")
  val startDate: String,
  @SerialName("end_date")
  val endDate: String,
  val partners: EventPartners,
  @SerialName("twitter_url")
  val twitterUrl: String?,
  @SerialName("linkedin_url")
  val linkedinUrl: String?,
  @SerialName("faq_link")
  val faqLink: String?,
  @SerialName("code_of_conduct_link")
  val codeOfConductLink: String?,
  @SerialName("updated_at")
  val updatedAt: Long
)
