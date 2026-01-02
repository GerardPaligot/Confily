package com.paligot.confily.backend.partners.infrastructure.firestore

import com.google.cloud.Timestamp
import com.paligot.confily.backend.addresses.infrastructure.firestore.AddressEntity
import com.paligot.confily.backend.addresses.infrastructure.firestore.convertToModel
import com.paligot.confily.backend.internals.helpers.storage.Upload
import com.paligot.confily.models.Job
import com.paligot.confily.models.Partner
import com.paligot.confily.models.PartnerMedia
import com.paligot.confily.models.PartnerMediaPngs
import com.paligot.confily.models.PartnerV2
import com.paligot.confily.models.PartnerV3
import com.paligot.confily.models.SocialItem
import com.paligot.confily.models.SocialType
import com.paligot.confily.models.inputs.PartnerInput
import java.net.URI

fun PartnerEntity.convertToModel() = Partner(
    name = this.name,
    logoUrl = this.logoUrl,
    siteUrl = this.siteUrl
)

fun PartnerEntity.convertToPartnerMediaModel(): PartnerMedia {
    if (media != null) {
        return PartnerMedia(
            svg = media.svg,
            pngs = PartnerMediaPngs(
                _250 = media.pngs._250,
                _500 = media.pngs._500,
                _1000 = media.pngs._1000
            )
        )
    }
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

fun PartnerEntity.convertToModelV2(jobs: List<Job>) = PartnerV2(
    id = this.id,
    name = this.name,
    description = this.description,
    logoUrl = this.logoUrl,
    videoUrl = this.videoUrl,
    media = convertToPartnerMediaModel(),
    siteUrl = this.siteUrl,
    twitterUrl = if (this.twitterUrl == "") null else this.twitterUrl,
    twitterMessage = this.twitterMessage,
    linkedinUrl = if (this.linkedinUrl == "") null else this.linkedinUrl,
    linkedinMessage = this.linkedinMessage,
    address = this.address.convertToModel(),
    jobs = jobs
)

fun PartnerEntity.convertToModelV3(jobs: List<Job>) = PartnerV3(
    id = this.id,
    name = this.name,
    description = this.description,
    media = convertToPartnerMediaModel(),
    videoUrl = videoUrl,
    types = this.sponsorings,
    socials = mutableListOf<SocialItem>().apply {
        add(SocialItem(type = SocialType.Website, url = this@convertToModelV3.siteUrl))
        val xUrl = this@convertToModelV3.twitterUrl
        if (xUrl != null && xUrl != "") {
            add(SocialItem(type = SocialType.X, url = xUrl))
        }
        val linkedUrl = this@convertToModelV3.linkedinUrl
        if (linkedUrl != null && linkedUrl != "") {
            add(SocialItem(type = SocialType.LinkedIn, url = linkedUrl))
        }
    },
    address = this.address.convertToModel(),
    jobs = jobs
)

@Suppress("ReturnCount")
fun List<Upload>.convertToPartnerMediaEntity(logoUrl: String): PartnerMediaEntity? {
    if (isEmpty()) return null
    val image250 = find { it.filename.contains("250.png") } ?: return null
    val image500 = find { it.filename.contains("500.png") } ?: return null
    val image1000 = find { it.filename.contains("1000.png") } ?: return null
    return PartnerMediaEntity(
        svg = logoUrl,
        pngs = PartnerPngsEntity(
            _250 = image250.url,
            _500 = image500.url,
            _1000 = image1000.url
        )
    )
}

fun PartnerInput.convertToEntity(
    id: String? = null,
    addressDb: AddressEntity,
    uploads: List<Upload> = emptyList()
) = PartnerEntity(
    id = id ?: "",
    name = name,
    description = description,
    logoUrl = logoUrl,
    media = uploads.convertToPartnerMediaEntity(logoUrl),
    videoUrl = videoUrl,
    siteUrl = siteUrl,
    twitterUrl = twitterUrl,
    twitterMessage = twitterMessage,
    linkedinUrl = linkedinUrl,
    linkedinMessage = linkedinMessage,
    address = addressDb,
    sponsoring = sponsorings.first(),
    sponsorings = sponsorings,
    wldId = wldId,
    creationDate = Timestamp.now()
)
