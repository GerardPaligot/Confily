package com.paligot.confily.backend.partners.application

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.PartnerFirestore
import com.paligot.confily.backend.internals.infrastructure.provider.CommonApi
import com.paligot.confily.backend.internals.infrastructure.storage.PartnerStorage
import com.paligot.confily.backend.internals.infrastructure.transcoder.Png
import com.paligot.confily.backend.internals.infrastructure.transcoder.TranscoderImage
import com.paligot.confily.backend.partners.domain.PartnerAdminRepository
import com.paligot.confily.backend.third.parties.geocode.GeocodeApi
import com.paligot.confily.backend.third.parties.geocode.convertToEntity
import com.paligot.confily.models.inputs.PartnerInput
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@Suppress("LongParameterList")
class PartnerAdminRepositoryDefault(
    private val geocodeApi: GeocodeApi,
    private val commonApi: CommonApi,
    private val eventDao: EventFirestore,
    private val partnerFirestore: PartnerFirestore,
    private val partnerStorage: PartnerStorage,
    private val imageTranscoder: TranscoderImage
) : PartnerAdminRepository {
    override suspend fun create(eventId: String, partnerInput: PartnerInput): String =
        coroutineScope {
            val event = eventDao.get(eventId)
            if (event.sponsoringTypes.any { partnerInput.sponsorings.contains(it) }.not()) {
                throw NotAcceptableException("Your sponsoring isn't valid")
            }
            val addressDb = geocodeApi.geocode(partnerInput.address).convertToEntity()
                ?: throw NotAcceptableException("Your address information isn't found")
            val partnerDb = partnerInput.convertToEntity(addressDb = addressDb)
            val id = partnerFirestore.createOrUpdate(eventId, partnerDb)
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
            val uploads = partnerStorage.uploadPartnerLogos(eventId, id, pngs)
            partnerFirestore.createOrUpdate(
                eventId,
                partnerDb.copy(
                    id = id,
                    media = uploads.convertToPartnerMediaEntity(partnerDb.logoUrl)
                )
            )
            return@coroutineScope id
        }

    override suspend fun update(eventId: String, partnerId: String, input: PartnerInput): String =
        coroutineScope {
            val event = eventDao.get(eventId)
            if (event.sponsoringTypes.any { input.sponsorings.contains(it) }.not()) {
                throw NotAcceptableException("Your sponsoring isn't valid")
            }
            val addressDb = geocodeApi.geocode(input.address).convertToEntity()
                ?: throw NotAcceptableException("Your address information isn't found")
            val pngs = listOf(
                async { imageTranscoder.convertSvgToPng(input.logoUrl, SIZE_250) },
                async { imageTranscoder.convertSvgToPng(input.logoUrl, SIZE_500) },
                async { imageTranscoder.convertSvgToPng(input.logoUrl, SIZE_1000) }
            ).awaitAll()
            val uploads = partnerStorage.uploadPartnerLogos(eventId, partnerId, pngs)
            val partnerDb =
                input.convertToEntity(id = partnerId, addressDb = addressDb, uploads = uploads)
            val id = partnerFirestore.createOrUpdate(eventId, partnerDb)
            return@coroutineScope id
        }

    companion object {
        const val SIZE_250 = 250
        const val SIZE_500 = 500
        const val SIZE_1000 = 1000
    }
}
