package com.paligot.confily.backend.partners.infrastructure.exposed

import com.paligot.confily.backend.addresses.infrastructure.exposed.toModel
import com.paligot.confily.models.Partner
import com.paligot.confily.models.PartnerMedia
import com.paligot.confily.models.PartnerMediaPngs
import com.paligot.confily.models.PartnerV2
import com.paligot.confily.models.PartnerV3
import com.paligot.confily.models.SocialType

fun PartnerEntity.toModelV1(): Partner {
    return Partner(
        name = this.name,
        logoUrl = this.mediaSvg ?: "",
        siteUrl = this.websiteUrl
    )
}

fun PartnerEntity.toModelV2(): PartnerV2 {
    val partnerId = this.id.value
    val socials = PartnerSocialsTable.socials(partnerId)
    val jobs = JobEntity
        .find { JobsTable.partnerId eq partnerId }
        .toList()

    return PartnerV2(
        id = partnerId.toString(),
        name = this.name,
        description = this.description ?: "",
        logoUrl = this.mediaSvg ?: "",
        videoUrl = this.videoUrl,
        media = PartnerMedia(
            svg = this.mediaSvg ?: "",
            pngs = if (this.mediaPng250 != null && this.mediaPng500 != null && this.mediaPng1000 != null) {
                PartnerMediaPngs(
                    _250 = this.mediaPng250!!,
                    _500 = this.mediaPng500!!,
                    _1000 = this.mediaPng1000!!
                )
            } else {
                null
            }
        ),
        siteUrl = this.websiteUrl,
        twitterUrl = socials.find { it.type == SocialType.X }?.url,
        twitterMessage = null,
        linkedinUrl = socials.find { it.type == SocialType.LinkedIn }?.url,
        linkedinMessage = null,
        address = this.address?.toModel(),
        jobs = jobs.map { job -> job.toModel(this.name) }
    )
}

fun PartnerEntity.toModelV3(): PartnerV3 {
    val partnerUuid = this.id.value
    val jobs = JobEntity
        .find { JobsTable.partnerId eq partnerUuid }
        .toList()
    return PartnerV3(
        id = this.id.value.toString(),
        name = this.name,
        description = this.description ?: "",
        siteUrl = this.websiteUrl,
        videoUrl = this.videoUrl,
        media = PartnerMedia(
            svg = this.mediaSvg ?: "",
            pngs = if (this.mediaPng250 != null && this.mediaPng500 != null && this.mediaPng1000 != null) {
                PartnerMediaPngs(
                    _250 = this.mediaPng250!!,
                    _500 = this.mediaPng500!!,
                    _1000 = this.mediaPng1000!!
                )
            } else {
                null
            }
        ),
        types = PartnerSponsorshipsTable.sponsoringTypeNamesByPartner(partnerUuid),
        address = this.address?.toModel(),
        socials = PartnerSocialsTable.socials(partnerUuid),
        jobs = jobs.map { job -> job.toModel(this.name) }
    )
}
