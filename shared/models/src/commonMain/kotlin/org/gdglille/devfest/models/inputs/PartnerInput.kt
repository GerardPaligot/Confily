package org.gdglille.devfest.models.inputs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartnerInput(
    val name: String,
    val description: String,
    @SerialName("logo_url")
    val logoUrl: String,
    @SerialName("site_url")
    val siteUrl: String,
    @SerialName("twitter_url")
    val twitterUrl: String?,
    @SerialName("twitter_message")
    val twitterMessage: String?,
    @SerialName("linkedin_url")
    val linkedinUrl: String?,
    @SerialName("linkedin_message")
    val linkedinMessage: String?,
    val address: String,
    val sponsoring: String,
    @SerialName("wld_id")
    val wldId: String?
) : Validator {
    override fun validate(): List<String> = emptyList()
}
