package com.paligot.confily.backend.events.infrastructure.firestore

import com.paligot.confily.models.EventV3
import com.paligot.confily.models.ExportEvent
import com.paligot.confily.models.SocialType

internal fun ExportEvent.convertToModelV3() = EventV3(
    id = this.id,
    name = this.name,
    address = this.address,
    startDate = this.startDate,
    endDate = this.endDate,
    menus = menus,
    coc = coc.content ?: "",
    openfeedbackProjectId = this.thirdParty.openfeedbackProjectId,
    features = features,
    contactPhone = this.contact.phone,
    contactEmail = this.contact.email ?: "",
    twitterUrl = this.contact.socials.find { it.type == SocialType.X }?.url,
    linkedinUrl = this.contact.socials.find { it.type == SocialType.LinkedIn }?.url,
    faqLink = this.qanda.link,
    codeOfConductLink = this.coc.link,
    updatedAt = this.updatedAt
)
