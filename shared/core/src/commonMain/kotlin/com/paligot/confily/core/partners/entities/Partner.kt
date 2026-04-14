package com.paligot.confily.core.partners.entities

import com.paligot.confily.core.socials.entities.Social
import com.paligot.confily.core.socials.entities.mapToSocialUi
import com.paligot.confily.core.speakers.entities.SpeakerItem
import com.paligot.confily.partners.ui.models.PartnerUi
import com.paligot.confily.speakers.ui.models.SpeakerItemUi
import kotlinx.collections.immutable.toImmutableList
import kotlin.native.ObjCName

@ObjCName("PartnerEntity")
class Partner(
    val info: PartnerInfo,
    val jobs: List<JobItem>,
    val socials: List<Social>,
    val speakers: List<SpeakerItem>
)

fun Partner.mapToPartnerUi(): PartnerUi = PartnerUi(
    id = info.id,
    name = info.name,
    description = info.description,
    logoUrl = info.logoUrl,
    videoUrl = info.videoUrl,
    formattedAddress = info.address?.formatted?.toImmutableList(),
    address = info.address?.formatted?.joinToString(", "),
    latitude = info.address?.latitude,
    longitude = info.address?.longitude,
    jobs = jobs.map { it.mapToJobUi() }.toImmutableList(),
    socials = socials.map { it.mapToSocialUi() }.toImmutableList(),
    speakers = speakers.map { it.mapToSpeakerItemUi() }.toImmutableList()
)

private fun SpeakerItem.mapToSpeakerItemUi(): SpeakerItemUi = SpeakerItemUi(
    id = id,
    name = displayName,
    activity = listOfNotNull(jobTitle, company).joinToString(" - "),
    url = photoUrl
)
