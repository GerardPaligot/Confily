package com.paligot.confily.backend.speakers.infrastructure.exposed

import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerSpeakerEntity
import com.paligot.confily.models.PartnerItem
import com.paligot.confily.models.Speaker

fun SpeakerEntity.toModel(): Speaker = Speaker(
    id = id.value.toString(),
    displayName = this.name,
    pronouns = this.pronouns,
    bio = this.bio ?: "",
    jobTitle = this.jobTitle,
    company = this.company,
    photoUrl = this.photoUrl ?: "",
    socials = SpeakerSocialsTable.socials(id.value),
    partners = PartnerSpeakerEntity.findBySpeaker(id.value).map { ps ->
        PartnerItem(
            id = ps.partner.id.value.toString(),
            name = ps.partner.name,
            logoUrl = ps.partner.mediaSvg ?: ""
        )
    }
)
