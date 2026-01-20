package com.paligot.confily.backend.third.parties.partnersconnect.application

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.addresses.infrastructure.exposed.toEntity
import com.paligot.confily.backend.addresses.infrastructure.provider.Geocode
import com.paligot.confily.backend.addresses.infrastructure.provider.GeocodeApi
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialEntity
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.JobsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerSocialsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerSponsorshipsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.SponsoringTypeEntity
import com.paligot.confily.backend.third.parties.partnersconnect.domain.PartnersConnectRepository
import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.InvoiceStatus
import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.PartnersConnectWebhookPayload
import com.paligot.confily.models.SocialType
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class PartnersConnectRepositoryExposed(
    private val database: Database,
    private val geocodeApi: GeocodeApi
) : PartnersConnectRepository {
    override suspend fun webhook(eventId: String, payload: PartnersConnectWebhookPayload): String = coroutineScope {
        val eventUuid = UUID.fromString(eventId)
        val headOffice = payload.company.headOffice
        val geocode = if (headOffice != null) {
            geocodeApi.geocode(headOffice.fullAddress)
        } else {
            null
        }
        return@coroutineScope transaction(db = database) {
            val event = EventEntity[eventUuid]
            val pack = payload.partnership.validatedPack
                ?: throw NotAcceptableException("Insert only validated partnership")
            if (payload.partnership.processStatus.billingStatus?.lowercase() != InvoiceStatus.PAID.name.lowercase()) {
                throw NotAcceptableException("Insert only paid partnership")
            }
            val partner = PartnerEntity
                .findByExternalId(
                    eventId = eventUuid,
                    externalId = payload.partnership.id,
                    provider = IntegrationProvider.PARTNERSCONNECT
                )
                ?.let { update(it, payload, geocode) }
                ?: create(event, payload, geocode)
            upsertSponsoringType(event, partner, pack.name)
            upsertSocials(partner, event, payload)
            upsertJobs(partner, payload)
            partner.id.value.toString()
        }
    }

    private fun create(event: EventEntity, payload: PartnersConnectWebhookPayload, geocode: Geocode?): PartnerEntity {
        val medias = payload.company.medias
        return PartnerEntity.new {
            this.event = event
            name = payload.company.name
            description = payload.company.description
            websiteUrl = payload.company.siteUrl
            if (medias != null) {
                mediaSvg = medias.original
                mediaPng250 = medias.png250
                mediaPng500 = medias.png500
                mediaPng1000 = medias.png1000
            }
            address = geocode?.toEntity()
            externalId = payload.partnership.id
            externalProvider = IntegrationProvider.PARTNERSCONNECT
        }
    }

    private fun update(
        partner: PartnerEntity,
        payload: PartnersConnectWebhookPayload,
        geocode: Geocode?
    ): PartnerEntity {
        val medias = payload.company.medias
        partner.name = payload.company.name
        partner.description = payload.company.description
        partner.websiteUrl = payload.company.siteUrl
        if (medias != null) {
            partner.mediaSvg = medias.original
            partner.mediaPng250 = medias.png250
            partner.mediaPng500 = medias.png500
            partner.mediaPng1000 = medias.png1000
        }
        partner.address = geocode?.toEntity()
        return partner
    }

    private fun upsertSponsoringType(
        event: EventEntity,
        partner: PartnerEntity,
        packName: String
    ): SponsoringTypeEntity {
        val sponsoringType = SponsoringTypeEntity
            .findByTypeName(event.id.value, packName)
            ?.let { entity ->
                entity.event = event
                entity.typeName = packName
                entity
            }
            ?: SponsoringTypeEntity.new {
                this.event = event
                this.typeName = packName
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

    private fun upsertSocials(partner: PartnerEntity, event: EventEntity, payload: PartnersConnectWebhookPayload) {
        SocialsTable.deleteWhere { SocialsTable.id inList PartnerSocialsTable.socialIds(partner.id.value) }
        val socialTypes = SocialType.entries.map { it.name.lowercase() }
        val socialIds = payload.company.socials
            .filter { socialTypes.contains(it.type.name.lowercase()) }
            .map { social ->
                val socialType = social.type.name.lowercase().replaceFirstChar { it.uppercase() }
                SocialEntity.new {
                    this.event = event
                    this.platform = SocialType.valueOf(socialType)
                    this.url = social.url
                }.id.value
            }
        PartnerSocialsTable.batchInsert(socialIds) { socialId ->
            this[PartnerSocialsTable.partnerId] = partner.id.value
            this[PartnerSocialsTable.socialId] = socialId
        }
    }

    private fun upsertJobs(partner: PartnerEntity, payload: PartnersConnectWebhookPayload) {
        JobsTable.deleteWhere { JobsTable.partnerId eq partner.id.value }
        JobsTable.batchInsert(payload.jobs) { job ->
            this[JobsTable.partnerId] = partner.id.value
            this[JobsTable.url] = job.url
            this[JobsTable.title] = job.title
            this[JobsTable.externalId] = job.id
            this[JobsTable.externalProvider] = IntegrationProvider.PARTNERSCONNECT
        }
    }
}
