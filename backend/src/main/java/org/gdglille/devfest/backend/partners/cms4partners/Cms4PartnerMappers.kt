package org.gdglille.devfest.backend.partners.cms4partners

import org.gdglille.devfest.backend.partners.convertToPartnerMediaModel
import org.gdglille.devfest.models.Address
import org.gdglille.devfest.models.Job
import org.gdglille.devfest.models.Partner
import org.gdglille.devfest.models.PartnerMedia
import org.gdglille.devfest.models.PartnerMediaPngs
import org.gdglille.devfest.models.PartnerV2
import java.net.URI

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

fun Cms4PartnerDb.convertToPartnerMediaModel(): PartnerMedia {
    val uri = URI.create(this.logoUrl)
    val path = this.logoUrl.split("?")[0]
    val pngs = if (path.endsWith(".svg")) {
        PartnerMediaPngs(
            _250 = "${path.replace(".svg", "-250.png")}?${uri.query}",
            _500 = "${path.replace(".svg", "-500.png")}?${uri.query}",
            _1000 = "${path.replace(".svg", "-1000.png")}?${uri.query}"
        )
    } else {
        PartnerMediaPngs(
            _250 = "$path-250.png?${uri.query}",
            _500 = "$path-500.png?${uri.query}",
            _1000 = "$path-1000.png?${uri.query}"
        )
    }
    return PartnerMedia(
        svg = this.logoUrl,
        pngs = pngs
    )
}

fun Cms4PartnerDb.convertToModelV2(jobs: List<Job>) = PartnerV2(
    id = this.id,
    name = this.name,
    description = this.description ?: "",
    logoUrl = this.logoUrl,
    media = convertToPartnerMediaModel(),
    siteUrl = this.siteUrl,
    twitterUrl = if (this.twitterAccount == "") null else this.twitterAccount,
    twitterMessage = this.twitter,
    linkedinUrl = if (this.linkedinAccount == "") null else this.linkedinAccount,
    linkedinMessage = this.linkedin,
    address = this.convertToAddressModel(),
    jobs = jobs
)
