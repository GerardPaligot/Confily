package org.gdglille.devfest.backend.partners.cms4partners

import org.gdglille.devfest.models.Address
import org.gdglille.devfest.models.Partner
import org.gdglille.devfest.models.PartnerV2

fun Cms4PartnerDb.convertToAddressModel(): Address? {
    if (location == null) return null
    return Address(
        formatted = listOf(
            address,
            zipcode,
            city
        ),
        address = address,
        country = "",
        countryCode = "",
        city = city,
        lat = this.location.lat,
        lng = this.location.lng
    )
}

fun Cms4PartnerDb.convertToModel() = Partner(
    name = this.name,
    logoUrl = this.logoUrl,
    siteUrl = this.siteUrl
)

fun Cms4PartnerDb.convertToModelV2() = PartnerV2(
    name = this.name,
    description = this.description ?: "",
    logoUrl = this.logoUrl,
    siteUrl = this.siteUrl,
    twitterUrl = this.twitterAccount,
    twitterMessage = this.twitter,
    linkedinUrl = this.linkedinAccount,
    linkedinMessage = this.linkedin,
    address = this.convertToAddressModel()
)
