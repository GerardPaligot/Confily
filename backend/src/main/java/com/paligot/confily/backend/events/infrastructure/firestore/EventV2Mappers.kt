package com.paligot.confily.backend.events.infrastructure.firestore

import com.paligot.confily.models.EventV2
import com.paligot.confily.models.ExportEvent
import com.paligot.confily.models.SocialType

internal fun ExportEvent.convertToModelV2(eventEntity: EventEntity) = EventV2(
    id = this.id,
    name = this.name,
    address = this.address,
    startDate = this.startDate,
    endDate = this.endDate,
    menus = menus,
    qanda = qanda.content[eventEntity.defaultLanguage] ?: emptyList(),
    coc = coc.content ?: "",
    openfeedbackProjectId = this.thirdParty.openfeedbackProjectId,
    features = features,
    contactPhone = contact.phone,
    contactEmail = contact.email ?: "",
    twitterUrl = contact.socials.find { it.type == SocialType.X }?.url,
    linkedinUrl = contact.socials.find { it.type == SocialType.LinkedIn }?.url,
    faqLink = this.qanda.link,
    codeOfConductLink = this.coc.link,
    updatedAt = this.updatedAt
)
