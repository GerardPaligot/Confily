package org.gdglille.devfest.backend.partners

import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.models.inputs.PartnerInput

class PartnerRepository(
    private val eventDao: EventDao,
    private val partnerDao: PartnerDao
) {
    suspend fun create(eventId: String, apiKey: String, partnerInput: PartnerInput) = coroutineScope {
        eventDao.getVerified(eventId, apiKey)
        val partnerDb = partnerInput.convertToDb()
        val id = partnerDao.createOrUpdate(eventId, partnerDb)
        eventDao.updateUpdatedAt(eventId)
        return@coroutineScope id
    }

    suspend fun update(eventId: String, apiKey: String, partnerId: String, partnerInput: PartnerInput) = coroutineScope {
        eventDao.getVerified(eventId, apiKey)
        val speakerDb = partnerInput.convertToDb(partnerId)
        eventDao.updateUpdatedAt(eventId)
        return@coroutineScope partnerDao.createOrUpdate(eventId, speakerDb)
    }
}