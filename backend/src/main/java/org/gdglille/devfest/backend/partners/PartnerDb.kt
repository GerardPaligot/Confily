package org.gdglille.devfest.backend.partners

import com.google.cloud.Timestamp
import org.gdglille.devfest.backend.events.AddressDb

data class PartnerDb(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val logoUrl: String = "",
    val siteUrl: String = "",
    val twitterUrl: String? = null,
    val twitterMessage: String? = null,
    val linkedinUrl: String? = null,
    val linkedinMessage: String? = null,
    val address: AddressDb = AddressDb(),
    val sponsoring: String = "",
    val wldId: String? = null,
    val creationDate: Timestamp = Timestamp.now()
)
