package com.paligot.conferences.backend.events

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class EventAddressDb(
    val address: String = "",
    val country: String = "",
    val countryCode: String = "",
    val city: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

data class EventDb(
    val id: String = "",
    val name: String = "",
    val address: EventAddressDb = EventAddressDb(),
    val startDate: String = "",
    val endDate: String = "",
    val twitterUrl: String? = null,
    val linkedinUrl: String? = null,
    val faqLink: String? = null,
    val codeOfConductLink: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)

@Serializable
data class EventAddressInput(
    val address: String,
    val country: String,
    @SerialName("country_code")
    val countryCode: String,
    val city: String,
    val lat: Double,
    val lng: Double
)

@Serializable
data class EventInput(
    val name: String,
    val address: EventAddressInput,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    @SerialName("twitter_url")
    val twitterUrl: String?,
    @SerialName("linkedin_url")
    val linkedinUrl: String?,
    @SerialName("faq_link")
    val faqLink: String,
    @SerialName("code_of_conduct_link")
    val codeOfConductLink: String,
    @SerialName("update_at")
    val updatedAt: Long = System.currentTimeMillis()
)
