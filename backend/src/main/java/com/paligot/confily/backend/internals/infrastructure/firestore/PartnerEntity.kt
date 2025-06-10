package com.paligot.confily.backend.internals.infrastructure.firestore

import com.google.cloud.Timestamp

@Suppress("ConstructorParameterNaming")
data class PartnerPngsEntity(
    val _250: String = "",
    val _500: String = "",
    val _1000: String = ""
)

data class PartnerMediaEntity(
    val svg: String = "",
    val pngs: PartnerPngsEntity = PartnerPngsEntity()
)

data class PartnerEntity(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val logoUrl: String = "",
    val media: PartnerMediaEntity? = null,
    val videoUrl: String? = null,
    val siteUrl: String = "",
    val twitterUrl: String? = null,
    val twitterMessage: String? = null,
    val linkedinUrl: String? = null,
    val linkedinMessage: String? = null,
    val address: AddressEntity = AddressEntity(),
    val sponsoring: String = "",
    val sponsorings: List<String> = emptyList(),
    val wldId: String? = null,
    val creationDate: Timestamp = Timestamp.now()
)
