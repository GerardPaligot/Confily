package com.paligot.confily.backend.partners

import com.google.cloud.Timestamp
import com.paligot.confily.backend.events.AddressDb

@Suppress("ConstructorParameterNaming")
data class PartnerPngsDb(
    val _250: String = "",
    val _500: String = "",
    val _1000: String = ""
)

data class PartnerMediaDb(
    val svg: String = "",
    val pngs: PartnerPngsDb = PartnerPngsDb()
)

data class PartnerDb(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val logoUrl: String = "",
    val media: PartnerMediaDb? = null,
    val siteUrl: String = "",
    val twitterUrl: String? = null,
    val twitterMessage: String? = null,
    val linkedinUrl: String? = null,
    val linkedinMessage: String? = null,
    val address: AddressDb = AddressDb(),
    val sponsoring: String = "",
    val sponsorings: List<String> = emptyList(),
    val wldId: String? = null,
    val creationDate: Timestamp = Timestamp.now()
)
