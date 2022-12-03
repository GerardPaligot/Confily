package org.gdglille.devfest.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Partner(
    val name: String,
    @SerialName("logo_url")
    val logoUrl: String,
    @SerialName("site_url")
    val siteUrl: String?
)

@Serializable
data class PartnerV2(
    val name: String,
    val description: String,
    @SerialName("logo_url")
    val logoUrl: String,
    @SerialName("site_url")
    val siteUrl: String?,
    @SerialName("twitter_url")
    val twitterUrl: String?,
    @SerialName("twitter_message")
    val twitterMessage: String?,
    @SerialName("linkedin_url")
    val linkedinUrl: String?,
    @SerialName("linkedin_message")
    val linkedinMessage: String?,
    val address: Address?
)
