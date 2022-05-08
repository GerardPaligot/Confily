package org.gdglille.devfest.backend.partners

import com.google.cloud.Timestamp

data class PartnerDb(
    val id: String = "",
    val logoUrl: String = "",
    val siteUrl: String? = null,
    val name: String = "",
    val sponsoring: String = "",
    val creationDate: Timestamp = Timestamp.now()
)
