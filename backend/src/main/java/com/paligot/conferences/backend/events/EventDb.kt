package com.paligot.conferences.backend.events

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
