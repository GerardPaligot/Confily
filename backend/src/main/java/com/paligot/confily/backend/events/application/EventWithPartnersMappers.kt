package com.paligot.confily.backend.events.application

import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.partners.Sponsorship
import com.paligot.confily.models.Event
import com.paligot.confily.models.EventPartners
import com.paligot.confily.models.ExportEvent
import com.paligot.confily.models.Partner
import com.paligot.confily.models.PartnerV3
import com.paligot.confily.models.PartnersActivities
import com.paligot.confily.models.SocialType

internal fun ExportEvent.convertToModel(eventEntity: EventEntity, partners: PartnersActivities) = Event(
    id = this.id,
    name = this.name,
    address = this.address,
    startDate = this.startDate,
    endDate = this.endDate,
    partners = EventPartners(
        golds = partners.partners.filter { it.types.contains(Sponsorship.Gold.name) }
            .map { it.convertToModel() }
            .sortedBy { it.name },
        silvers = partners.partners.filter { it.types.contains(Sponsorship.Silver.name) }
            .map { it.convertToModel() }
            .sortedBy { it.name },
        bronzes = partners.partners.filter { it.types.contains(Sponsorship.Bronze.name) }
            .map { it.convertToModel() }
            .sortedBy { it.name },
        others = partners.partners.filter { it.types.contains(Sponsorship.Other.name) }
            .map { it.convertToModel() }
            .sortedBy { it.name }
    ),
    menus = menus,
    qanda = qanda.content[eventEntity.defaultLanguage] ?: emptyList(),
    coc = coc.content ?: "",
    features = features,
    twitterUrl = this.contact.socials.find { it.type == SocialType.X }?.url,
    linkedinUrl = this.contact.socials.find { it.type == SocialType.LinkedIn }?.url,
    faqLink = this.qanda.link,
    codeOfConductLink = this.coc.link,
    updatedAt = this.updatedAt
)

private fun PartnerV3.convertToModel() = Partner(
    name = this.name,
    logoUrl = this.media.pngs?._250 ?: this.media.svg,
    siteUrl = this.socials.find { it.type == SocialType.Website }?.url
)
