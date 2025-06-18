package com.paligot.confily.backend.export.application

import com.paligot.confily.backend.activities.application.convertToModel
import com.paligot.confily.backend.export.domain.ExportAdminRepository
import com.paligot.confily.backend.internals.infrastructure.firestore.ActivityFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.EventEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.JobFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.PartnerFirestore
import com.paligot.confily.backend.internals.infrastructure.storage.EventStorage
import com.paligot.confily.backend.partners.application.convertToModelV3
import com.paligot.confily.backend.third.parties.welovedevs.application.convertToModel
import com.paligot.confily.models.PartnersActivities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ExportPartnersAdminRepositoryDefault(
    private val eventFirestore: EventFirestore,
    private val eventStorage: EventStorage,
    private val partnerFirestore: PartnerFirestore,
    private val jobFirestore: JobFirestore,
    private val activityFirestore: ActivityFirestore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExportAdminRepository<PartnersActivities> {
    override suspend fun export(eventId: String): PartnersActivities {
        val eventDb = eventFirestore.get(eventId)
        val partners = buildPartnersActivities(eventDb)
        eventStorage.uploadPartnersFile(eventId, eventDb.partnersUpdatedAt, partners)
        return partners
    }

    private suspend fun buildPartnersActivities(eventDb: EventEntity): PartnersActivities =
        coroutineScope {
            val partners = async(dispatcher) { partnerFirestore.getAll(eventDb.slugId) }
            val jobs = async(dispatcher) { jobFirestore.getAll(eventDb.slugId) }
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
