package org.gdglille.devfest.backend.partners

import org.gdglille.devfest.backend.NotAcceptableException
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.internals.network.geolocation.GeocodeApi
import org.gdglille.devfest.backend.internals.network.geolocation.convertToDb
import org.gdglille.devfest.backend.jobs.JobDao
import org.gdglille.devfest.backend.jobs.convertToModel
import org.gdglille.devfest.backend.partners.cms4partners.Cms4PartnersDao
import org.gdglille.devfest.backend.partners.cms4partners.convertToModelV2
import org.gdglille.devfest.models.PartnerV2
import org.gdglille.devfest.models.inputs.PartnerInput

class PartnerRepository(
    private val geocodeApi: GeocodeApi,
    private val eventDao: EventDao,
    private val partnerDao: PartnerDao,
    private val cms4PartnersDao: Cms4PartnersDao,
    private val jobDao: JobDao
) {
    suspend fun list(eventId: String): Map<String, List<PartnerV2>> {
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val partners = partnerDao.getAll(eventId)
        val companies = cms4PartnersDao.list(year = event.year)
        val jobs = jobDao.getAll(eventId)
        return event.sponsoringTypes.associateWith { sponsoring ->
            companies
                .filter { it.sponsoring == sponsoring }
                .map { partner ->
                    partner.convertToModelV2(
                        jobs.filter { it.partnerId == partner.id }
                            .map { it.convertToModel() }
                    )
                }
                .plus(
                    partners
                        .filter { it.sponsoring == sponsoring }
                        .map { partner ->
                            partner.convertToModelV2(
                                jobs.filter { it.partnerId == partner.id }
                                    .map { it.convertToModel() }
                            )
                        }
                )
                .sortedBy { it.name }
        }
    }

    suspend fun create(eventId: String, apiKey: String, partnerInput: PartnerInput): String {
        val event = eventDao.getVerified(eventId, apiKey)
        if (event.sponsoringTypes.contains(partnerInput.sponsoring).not()) {
            throw NotAcceptableException("Your sponsoring isn't valid")
        }
        val addressDb = geocodeApi.geocode(partnerInput.address).convertToDb()
            ?: throw NotAcceptableException("Your address information isn't found")
        val partnerDb = partnerInput.convertToDb(addressDb = addressDb)
        val id = partnerDao.createOrUpdate(eventId, partnerDb)
        eventDao.updateUpdatedAt(event)
        return id
    }

    suspend fun update(
        eventId: String,
        apiKey: String,
        partnerId: String,
        partnerInput: PartnerInput
    ): String {
        val event = eventDao.getVerified(eventId, apiKey)
        if (event.sponsoringTypes.contains(partnerInput.sponsoring).not()) {
            throw NotAcceptableException("Your sponsoring isn't valid")
        }
        val addressDb = geocodeApi.geocode(partnerInput.address).convertToDb()
            ?: throw NotAcceptableException("Your address information isn't found")
        val partnerDb = partnerInput.convertToDb(id = partnerId, addressDb = addressDb)
        val id = partnerDao.createOrUpdate(eventId, partnerDb)
        eventDao.updateUpdatedAt(event)
        return id
    }
}
