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
