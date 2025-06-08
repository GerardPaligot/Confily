@file:Suppress("TooManyFunctions")

package com.paligot.confily.backend.export

import com.paligot.confily.backend.internals.application.convertToModel
import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.LunchMenuEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.SocialEntity
import com.paligot.confily.models.CodeOfConduct
import com.paligot.confily.models.EventContact
import com.paligot.confily.models.EventMap
import com.paligot.confily.models.ExportEvent
import com.paligot.confily.models.FeaturesActivated
import com.paligot.confily.models.QAndA
import com.paligot.confily.models.QuestionAndResponse
import com.paligot.confily.models.TeamMember
import com.paligot.confily.models.ThirdParty

fun EventEntity.convertToModelV5(
    qanda: Map<String, List<QuestionAndResponse>>,
    team: Map<String, List<TeamMember>>,
    maps: List<EventMap>,
    hasPartners: Boolean
) = ExportEvent(
    id = this.slugId,
    name = this.name,
    address = this.address.convertToModel(),
    startDate = this.startDate,
    endDate = this.endDate,
    contact = EventContact(
        phone = this.contactPhone,
        email = this.contactEmail,
        socials = this.socials.map(SocialEntity::convertToModel)
    ),
    coc = CodeOfConduct(content = coc, link = codeOfConductLink),
    qanda = QAndA(content = qanda, link = faqLink),
    menus = menus.map(LunchMenuEntity::convertToModel),
    features = FeaturesActivated(
        hasNetworking = features.hasNetworking,
        hasSpeakerList = !features.hasNetworking,
        hasPartnerList = hasPartners,
        hasMenus = menus.isNotEmpty(),
        hasQAndA = qanda.isNotEmpty(),
        hasBilletWebTicket = billetWebConfig != null
    ),
    team = team,
    maps = maps,
    thirdParty = ThirdParty(openfeedbackProjectId = this.openFeedbackId),
    updatedAt = this.eventUpdatedAt
)
