package com.paligot.confily.core.partners.entities

import com.paligot.confily.core.socials.entities.Social
import com.paligot.confily.core.socials.entities.mapToSocialUi
import com.paligot.confily.partners.ui.models.PartnerUi
import kotlinx.collections.immutable.toImmutableList
import kotlin.native.ObjCName

@ObjCName("PartnerEntity")
class Partner(
    val info: PartnerInfo,
    val jobs: List<JobItem>,
    val socials: List<Social>
)

fun Partner.mapToPartnerUi(): PartnerUi = PartnerUi(
    id = info.id,
    name = info.name,
    description = info.description,
    logoUrl = info.logoUrl,
    formattedAddress = info.address?.formatted?.toImmutableList(),
    address = info.address?.formatted?.joinToString(", "),
    latitude = info.address?.latitude,
    longitude = info.address?.longitude,
    jobs = jobs.map { it.mapToJobUi() }.toImmutableList(),
    socials = socials.map { it.mapToSocialUi() }.toImmutableList()
)
