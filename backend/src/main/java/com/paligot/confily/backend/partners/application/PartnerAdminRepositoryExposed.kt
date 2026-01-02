package com.paligot.confily.backend.partners.application

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.addresses.infrastructure.exposed.toEntity
import com.paligot.confily.backend.addresses.infrastructure.provider.GeocodeApi
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.internals.helpers.slug
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialEntity
import com.paligot.confily.backend.internals.infrastructure.exposed.SocialsTable
import com.paligot.confily.backend.internals.infrastructure.provider.CommonApi
import com.paligot.confily.backend.internals.infrastructure.transcoder.Png
import com.paligot.confily.backend.internals.infrastructure.transcoder.TranscoderImage
import com.paligot.confily.backend.partners.application.PartnerAdminRepositoryDefault.Companion.SIZE_1000
import com.paligot.confily.backend.partners.application.PartnerAdminRepositoryDefault.Companion.SIZE_250
import com.paligot.confily.backend.partners.application.PartnerAdminRepositoryDefault.Companion.SIZE_500
import com.paligot.confily.backend.partners.domain.PartnerAdminRepository
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerEntity
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerSocialsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.PartnerSponsorshipsTable
import com.paligot.confily.backend.partners.infrastructure.exposed.SponsoringTypeEntity
import com.paligot.confily.backend.partners.infrastructure.storage.PartnerStorage
import com.paligot.confily.models.SocialType
import com.paligot.confily.models.inputs.PartnerInput
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class PartnerAdminRepositoryExposed(
    private val database: Database,
    private val commonApi: CommonApi,
    private val geocodeApi: GeocodeApi,
    private val partnerStorage: PartnerStorage,
    private val imageTranscoder: TranscoderImage
) : PartnerAdminRepository {

    override suspend fun create(eventId: String, partnerInput: PartnerInput): String = coroutineScope {
        val eventUuid = UUID.fromString(eventId)
        val partnerUuid = UUID.randomUUID()
        val event = transaction(db = database) { EventEntity[eventUuid] }
        val address = geocodeApi.geocode(partnerInput.address)
        val pngs = if (partnerInput.logoUrl.endsWith(".png")) {
            val content = commonApi.fetchByteArray(partnerInput.logoUrl)
            listOf(
                Png(content = content, size = 250),
                Png(content = content, size = 500),
                Png(content = content, size = 1000)
            )
        } else {
            listOf(
                async { imageTranscoder.convertSvgToPng(partnerInput.logoUrl, SIZE_250) },
                async { imageTranscoder.convertSvgToPng(partnerInput.logoUrl, SIZE_500) },
                async { imageTranscoder.convertSvgToPng(partnerInput.logoUrl, SIZE_1000) }
            ).awaitAll()
        }
        val uploads = partnerStorage.uploadPartnerLogos(event.slug, partnerInput.name.slug(), pngs)
        return@coroutineScope transaction(db = database) {
            val sponsoringTypes = SponsoringTypeEntity
                .findByTypeNames(eventUuid, partnerInput.sponsorings)

            if (sponsoringTypes.any { partnerInput.sponsorings.contains(it.typeName) }.not()) {
                throw NotAcceptableException("Your sponsoring isn't valid")
            }

            // Create the partner
            PartnerEntity.new(partnerUuid) {
                this.event = event
                this.name = partnerInput.name
                this.description = partnerInput.description
                this.websiteUrl = partnerInput.siteUrl
                this.mediaSvg = partnerInput.logoUrl
                this.mediaPng250 = uploads.find { it.filename.contains("250.png") }?.url
                this.mediaPng500 = uploads.find { it.filename.contains("500.png") }?.url
                this.mediaPng1000 = uploads.find { it.filename.contains("1000.png") }?.url
                this.videoUrl = partnerInput.videoUrl
                this.address = address.toEntity()
                    ?: throw NotAcceptableException("Your address information isn't found")
            }

            // Create sponsorship relationships
            if (partnerInput.sponsorings.isNotEmpty()) {
                PartnerSponsorshipsTable.batchInsert(sponsoringTypes.map { it.id }) { typeId ->
                    this[PartnerSponsorshipsTable.partnerId] = partnerUuid
                    this[PartnerSponsorshipsTable.sponsoringTypeId] = typeId
                }
            }

            // Create social entries
            val socials = mutableListOf<Pair<SocialType, String>>()
            partnerInput.twitterUrl?.let { socials.add(SocialType.X to it) }
            partnerInput.linkedinUrl?.let { socials.add(SocialType.LinkedIn to it) }

            if (socials.isNotEmpty()) {
                val socialIds = socials.map { (platform, url) ->
                    SocialEntity.new {
                        this.event = event
                        this.platform = platform
                        this.url = url
                    }.id.value
                }
                PartnerSocialsTable.batchInsert(socialIds) { socialId ->
                    this[PartnerSocialsTable.partnerId] = partnerUuid
                    this[PartnerSocialsTable.socialId] = socialId
                }
            }

            partnerUuid.toString()
        }
    }

    override suspend fun update(eventId: String, partnerId: String, input: PartnerInput): String = coroutineScope {
        val eventUuid = UUID.fromString(eventId)
        val partnerUuid = UUID.fromString(partnerId)
        val event = transaction(db = database) { EventEntity[eventUuid] }
        val address = geocodeApi.geocode(input.address)
        val pngs = if (input.logoUrl.endsWith(".png")) {
            val content = commonApi.fetchByteArray(input.logoUrl)
            listOf(
                Png(content = content, size = 250),
                Png(content = content, size = 500),
                Png(content = content, size = 1000)
            )
        } else {
            listOf(
                async { imageTranscoder.convertSvgToPng(input.logoUrl, SIZE_250) },
                async { imageTranscoder.convertSvgToPng(input.logoUrl, SIZE_500) },
                async { imageTranscoder.convertSvgToPng(input.logoUrl, SIZE_1000) }
            ).awaitAll()
        }
        val uploads = partnerStorage.uploadPartnerLogos(event.slug, input.name.slug(), pngs)
        return@coroutineScope transaction(db = database) {
            val partner = PartnerEntity
                .findById(eventUuid, partnerUuid)
                ?: throw NotFoundException("Partner not found: $partnerId")

            // Update partner fields
            partner.name = input.name
            partner.description = input.description
            partner.websiteUrl = input.siteUrl
            partner.mediaSvg = input.logoUrl
            partner.mediaPng250 = uploads.find { it.filename.contains("250.png") }?.url
            partner.mediaPng500 = uploads.find { it.filename.contains("500.png") }?.url
            partner.mediaPng1000 = uploads.find { it.filename.contains("1000.png") }?.url
            partner.videoUrl = input.videoUrl
            partner.address = address.toEntity()
                ?: throw NotAcceptableException("Your address information isn't found")

            // Update sponsorship relationships
            PartnerSponsorshipsTable.deleteWhere { PartnerSponsorshipsTable.partnerId eq partnerUuid }
            if (input.sponsorings.isNotEmpty()) {
                val sponsoringTypes = SponsoringTypeEntity
                    .findByTypeNames(eventUuid, input.sponsorings)
                PartnerSponsorshipsTable.batchInsert(sponsoringTypes.map { it.id }) { typeId ->
                    this[PartnerSponsorshipsTable.partnerId] = partnerUuid
                    this[PartnerSponsorshipsTable.sponsoringTypeId] = typeId
                }
            }

            // Delete old socials
            val socialIds = PartnerSocialsTable.socialIds(partnerUuid)

            PartnerSocialsTable.deleteWhere { PartnerSocialsTable.partnerId eq partnerUuid }
            if (socialIds.isNotEmpty()) {
                SocialsTable.deleteWhere { SocialsTable.id inList socialIds }
            }

            // Create new socials
            val socials = mutableListOf<Pair<SocialType, String>>()
            input.twitterUrl?.let { socials.add(SocialType.X to it) }
            input.linkedinUrl?.let { socials.add(SocialType.LinkedIn to it) }

            if (socials.isNotEmpty()) {
                val socialIds = socials.map { (platform, url) ->
                    SocialEntity.new {
                        this.event = event
                        this.platform = platform
                        this.url = url
                    }.id.value
                }
                PartnerSocialsTable.batchInsert(socialIds) { socialId ->
                    this[PartnerSocialsTable.partnerId] = partnerUuid
                    this[PartnerSocialsTable.socialId] = socialId
                }
            }

            partner.id.value.toString()
        }
    }
}
