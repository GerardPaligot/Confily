package com.paligot.confily.backend.partners.infrastructure.exposed

import com.paligot.confily.backend.addresses.infrastructure.exposed.toModel
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialsTable
import com.paligot.confily.models.PartnerMedia
import com.paligot.confily.models.PartnerMediaPngs
import com.paligot.confily.models.PartnerV2
import com.paligot.confily.models.PartnerV3
import com.paligot.confily.models.SocialItem
import com.paligot.confily.models.SocialType
import org.jetbrains.exposed.sql.selectAll

fun PartnerEntity.toModel(): PartnerV2 {
    // Fetch socials for this partner from the junction table
    val partnerId = this.id.value
    val socials = PartnerSocialsTable
        .innerJoin(SocialsTable)
        .selectAll()
        .where { PartnerSocialsTable.partnerId eq partnerId }
        .map { row ->
            val platform = row[SocialsTable.platform]
            val url = row[SocialsTable.url]
            Pair(platform, url)
        }
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
        twitterUrl = socials.find { it.first == SocialType.X }?.second,
        twitterMessage = null,
        linkedinUrl = socials.find { it.first == SocialType.LinkedIn }?.second,
        linkedinMessage = null,
        address = this.address?.toModel(),
        jobs = jobs.map { job -> job.toModel(this.name) }
    )
}

fun PartnerEntity.toModelV3(): PartnerV3 {
    val partnerUuid = this.id.value
    val sponsoringTypes = PartnerSponsorshipsTable
        .innerJoin(SponsoringTypesTable)
        .selectAll()
        .where { PartnerSponsorshipsTable.partnerId eq partnerUuid }
        .map { it[SponsoringTypesTable.typeName] }
    val socials = PartnerSocialsTable
        .innerJoin(SocialsTable)
        .selectAll()
        .where { PartnerSocialsTable.partnerId eq partnerUuid }
        .map { row ->
            val platform = row[SocialsTable.platform]
            val url = row[SocialsTable.url]
            Pair(platform, url)
        }
    val jobs = JobEntity
        .find { JobsTable.partnerId eq partnerUuid }
        .toList()
    return PartnerV3(
        id = this.id.value.toString(),
        name = this.name,
        description = this.description ?: "",
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
        types = sponsoringTypes,
        address = this.address?.toModel(),
        socials = socials.map { SocialItem(type = it.first, url = it.second) },
        jobs = jobs.map { job -> job.toModel(this.name) }
    )
}
