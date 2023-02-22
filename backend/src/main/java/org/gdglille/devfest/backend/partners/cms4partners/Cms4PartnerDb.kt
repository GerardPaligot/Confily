package org.gdglille.devfest.backend.partners.cms4partners

import com.google.cloud.Timestamp

data class Cms4LocationDb(
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

data class Cms4PartnerDb(
    val id: String = "",
    val edition: String = "",
    val name: String = "",
    val description: String? = null,
    val logoUrl: String = "",
    val siteUrl: String? = null,
    val address: String = "",
    val city: String = "",
    val zipCode: String = "",
    val location: Cms4LocationDb? = null,
    val twitter: String? = null,
    val twitterAccount: String? = null,
    val linkedin: String? = null,
    val linkedinAccount: String? = null,
    val sponsoring: String = "",
    val wldId: String? = null,
    val creationDate: Timestamp = Timestamp.now()
)
