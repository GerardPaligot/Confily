package com.paligot.confily.backend.partners

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.internals.helpers.image.TranscoderImage
import com.paligot.confily.backend.jobs.JobDao
import com.paligot.confily.backend.third.parties.geocode.GeocodeApi
import com.paligot.confily.backend.third.parties.geocode.convertToDb
import com.paligot.confily.backend.third.parties.welovedevs.convertToModel
import com.paligot.confily.models.PartnerV2
import com.paligot.confily.models.inputs.PartnerInput
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PartnerRepository(
    private val geocodeApi: GeocodeApi,
    private val eventDao: EventDao,
    private val partnerDao: PartnerDao,
    private val jobDao: JobDao,
    private val imageTranscoder: TranscoderImage
) {
    suspend fun list(eventId: String): Map<String, List<PartnerV2>> {
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val partners = partnerDao.getAll(eventId)
        val jobs = jobDao.getAll(eventId)
        return event.sponsoringTypes.associateWith { sponsoring ->
            partners
                .filter { it.sponsorings.contains(sponsoring) }
                .map { partner ->
                    partner.convertToModelV2(
                        jobs.filter { it.partnerId == partner.id }
                            .map { it.convertToModel() }
                    )
                }
                .sortedBy { it.name }
        }
    }

    suspend fun create(eventId: String, apiKey: String, partnerInput: PartnerInput): String =
        coroutineScope {
            val event = eventDao.getVerified(eventId, apiKey)
            if (event.sponsoringTypes.any { partnerInput.sponsorings.contains(it) }.not()) {
                throw NotAcceptableException("Your sponsoring isn't valid")
            }
            val addressDb = geocodeApi.geocode(partnerInput.address).convertToDb()
                ?: throw NotAcceptableException("Your address information isn't found")
            val partnerDb = partnerInput.convertToDb(addressDb = addressDb)
            val id = partnerDao.createOrUpdate(eventId, partnerDb)
            val pngs = listOf(
                async { imageTranscoder.convertSvgToPng(partnerInput.logoUrl, SIZE_250) },
                async { imageTranscoder.convertSvgToPng(partnerInput.logoUrl, SIZE_500) },
                async { imageTranscoder.convertSvgToPng(partnerInput.logoUrl, SIZE_1000) }
            ).awaitAll()
            val uploads = partnerDao.uploadPartnerLogos(eventId, id, pngs)
            partnerDao.createOrUpdate(
                eventId,
                partnerDb.copy(
                    id = id,
                    media = uploads.convertToPartnerMediaDb(partnerDb.logoUrl)
                )
            )
            eventDao.updateUpdatedAt(event)
            return@coroutineScope id
        }

    suspend fun update(
        eventId: String,
        apiKey: String,
        partnerId: String,
        partnerInput: PartnerInput
    ): String = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        if (event.sponsoringTypes.any { partnerInput.sponsorings.contains(it) }.not()) {
            throw NotAcceptableException("Your sponsoring isn't valid")
        }
        val addressDb = geocodeApi.geocode(partnerInput.address).convertToDb()
            ?: throw NotAcceptableException("Your address information isn't found")
        val pngs = listOf(
            async { imageTranscoder.convertSvgToPng(partnerInput.logoUrl, SIZE_250) },
            async { imageTranscoder.convertSvgToPng(partnerInput.logoUrl, SIZE_500) },
            async { imageTranscoder.convertSvgToPng(partnerInput.logoUrl, SIZE_1000) }
        ).awaitAll()
        val uploads = partnerDao.uploadPartnerLogos(eventId, partnerId, pngs)
        val partnerDb =
            partnerInput.convertToDb(id = partnerId, addressDb = addressDb, uploads = uploads)
        val id = partnerDao.createOrUpdate(eventId, partnerDb)
        eventDao.updateUpdatedAt(event)
        return@coroutineScope id
    }

    companion object {
        const val SIZE_250 = 250
        const val SIZE_500 = 500
        const val SIZE_1000 = 1000
    }
}
