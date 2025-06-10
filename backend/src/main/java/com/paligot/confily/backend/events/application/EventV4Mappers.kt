package com.paligot.confily.backend.events.application

import com.paligot.confily.models.EventV4
import com.paligot.confily.models.ExportEvent

fun ExportEvent.convertToModelV4(): EventV4 = EventV4(
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
    socials = this.contact.socials,
    faqLink = this.qanda.link,
    codeOfConductLink = this.coc.link,
    updatedAt = this.updatedAt
)
