package org.gdglille.devfest.backend.partners.cms4partners

import org.gdglille.devfest.models.Address
import org.gdglille.devfest.models.Job
import org.gdglille.devfest.models.Partner
import org.gdglille.devfest.models.PartnerV2

fun Cms4PartnerDb.convertToAddressModel(): Address? {
    if (location == null) return null
    return Address(
        formatted = listOf(
            address,
            "$zipCode $city"
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

fun Cms4PartnerDb.convertToModelV2(jobs: List<Job>) = PartnerV2(
    id = this.id,
    name = this.name,
    description = this.description ?: "",
    logoUrl = this.logoUrl,
    siteUrl = this.siteUrl,
    twitterUrl = if (this.twitterAccount == "") null else this.twitterAccount,
    twitterMessage = this.twitter,
    linkedinUrl = if (this.linkedinAccount == "") null else this.linkedinAccount,
    linkedinMessage = this.linkedin,
    address = this.convertToAddressModel(),
    jobs = jobs
)
