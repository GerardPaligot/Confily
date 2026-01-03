package com.paligot.confily.backend.third.parties.openplanner.infrastructure.exposed

import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.internals.helpers.storage.Upload
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerSponsorshipsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.SponsoringTypeEntity
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.SponsorGroupOP
import org.jetbrains.exposed.sql.insert

fun SponsorGroupOP.toEntity(event: EventEntity, remoteUrls: Map<String, List<Upload>>): List<PartnerEntity> =
    this.sponsors.map { sponsor ->
        val uploads = remoteUrls[sponsor.id] ?: emptyList()
        val partner = PartnerEntity
            .findByExternalId(
                eventId = event.id.value,
                externalId = sponsor.id,
                provider = IntegrationProvider.OPENPLANNER
            )
            ?.let { entity ->
                entity.name = sponsor.name
                entity.websiteUrl = sponsor.website
                if (uploads.isNotEmpty()) {
                    entity.mediaSvg = sponsor.logoUrl
                    entity.mediaPng250 = uploads.find { it.filename.contains("250.png") }?.url
                    entity.mediaPng500 = uploads.find { it.filename.contains("500.png") }?.url
                    entity.mediaPng1000 = uploads.find { it.filename.contains("1000.png") }?.url
                }
                entity
            }
            ?: PartnerEntity.new {
                this.event = event
                name = sponsor.name
                websiteUrl = sponsor.website
                if (uploads.isNotEmpty()) {
                    mediaSvg = sponsor.logoUrl
                    mediaPng250 = uploads.find { it.filename.contains("250.png") }?.url
                    mediaPng500 = uploads.find { it.filename.contains("500.png") }?.url
                    mediaPng1000 = uploads.find { it.filename.contains("1000.png") }?.url
                }
                externalId = sponsor.id
                externalProvider = IntegrationProvider.OPENPLANNER
            }
        upsertSponsoringType(event = event, partner = partner, groupName = this.name, order = this.order)
        partner
    }

private fun upsertSponsoringType(
    event: EventEntity,
    partner: PartnerEntity,
    groupName: String,
    order: Int
): SponsoringTypeEntity {
    val sponsoringType = SponsoringTypeEntity
        .findByTypeName(event.id.value, groupName)
        ?.let { entity ->
            entity.event = event
            entity.typeName = groupName
            entity.displayOrder = order
            entity
        }
        ?: SponsoringTypeEntity.new {
            this.event = event
            this.typeName = groupName
            this.displayOrder = order
        }
    val exist = PartnerSponsorshipsTable
        .partnerExist(partnerId = partner.id.value, sponsoringTypeId = sponsoringType.id.value)
    if (!exist) {
        PartnerSponsorshipsTable.insert {
            it[PartnerSponsorshipsTable.partnerId] = partner.id.value
            it[PartnerSponsorshipsTable.sponsoringTypeId] = sponsoringType.id.value
        }
    }
    return sponsoringType
}
