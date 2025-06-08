package com.paligot.confily.backend.export

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.activities.application.convertToModel
import com.paligot.confily.backend.internals.infrastructure.firestore.ActivityFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.storage.EventStorage
import com.paligot.confily.backend.jobs.JobDao
import com.paligot.confily.backend.partners.PartnerDao
import com.paligot.confily.backend.partners.convertToModelV3
import com.paligot.confily.backend.third.parties.welovedevs.convertToModel
import com.paligot.confily.models.PartnersActivities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ExportPartnersRepository(
    private val eventFirestore: EventFirestore,
    private val eventStorage: EventStorage,
    private val partnerDao: PartnerDao,
    private val jobDao: JobDao,
    private val activityFirestore: ActivityFirestore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun get(eventId: String): PartnersActivities {
        val eventDb = eventFirestore.get(eventId)
        return eventStorage.getPartnersFile(eventId, eventDb.partnersUpdatedAt)
            ?: throw NotFoundException("Partners $eventId Not Found")
    }

    suspend fun export(eventId: String) = coroutineScope {
        val eventDb = eventFirestore.get(eventId)
        val partners = buildPartnersActivities(eventDb)
        eventStorage.uploadPartnersFile(eventId, eventDb.partnersUpdatedAt, partners)
        return@coroutineScope partners
    }

    private suspend fun buildPartnersActivities(eventDb: EventEntity): PartnersActivities =
        coroutineScope {
            val partners = async(dispatcher) { partnerDao.getAll(eventDb.slugId) }
            val jobs = async(dispatcher) { jobDao.getAll(eventDb.slugId) }
            val activities = async(dispatcher) { activityFirestore.getAll(eventDb.slugId) }
            val fetchedJobs = jobs.await()
            return@coroutineScope PartnersActivities(
                types = eventDb.sponsoringTypes,
                partners = partners.await().map { partner ->
                    partner.convertToModelV3(
                        fetchedJobs
                            .filter { it.partnerId == partner.id }
                            .map { it.convertToModel() }
                    )
                },
                activities = activities.await()
                    .sortedBy { it.startTime }
                    .map { it.convertToModel() }
            )
        }
}
