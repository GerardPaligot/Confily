package org.gdglille.devfest.models.inputs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PartnerInput(
    @SerialName("logo_url")
    val logoUrl: String,
    @SerialName("site_url")
    val siteUrl: String,
    val name: String,
    val sponsoring: String
): Validator {
    override fun validate(): List<String> = emptyList()
}
