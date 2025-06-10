package com.paligot.confily.backend.partners.application

import com.paligot.confily.backend.activities.application.convertToModel
import com.paligot.confily.backend.internals.infrastructure.firestore.ActivityEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.ActivityFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.JobEntity
import com.paligot.confily.backend.internals.infrastructure.firestore.JobFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.PartnerFirestore
import com.paligot.confily.backend.partners.domain.PartnerRepository
import com.paligot.confily.backend.third.parties.welovedevs.application.convertToModel
import com.paligot.confily.models.PartnerV2
import com.paligot.confily.models.PartnersActivities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class PartnerRepositoryDefault(
    private val eventDao: EventFirestore,
    private val partnerFirestore: PartnerFirestore,
    private val activityFirestore: ActivityFirestore,
    private val jobFirestore: JobFirestore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : PartnerRepository {
    override fun list(eventId: String): Map<String, List<PartnerV2>> {
        val event = eventDao.get(eventId)
        val partners = partnerFirestore.getAll(eventId)
        val jobs = jobFirestore.getAll(eventId)
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

    override suspend fun activities(eventId: String): PartnersActivities = coroutineScope {
        val event = eventDao.get(eventId)
        val partners = async(dispatcher) { partnerFirestore.getAll(eventId) }
        val jobs = async(dispatcher) { jobFirestore.getAll(eventId) }
        val activities = async(dispatcher) { activityFirestore.getAll(eventId) }
        val fetchedJobs = jobs.await()
        return@coroutineScope PartnersActivities(
            types = event.sponsoringTypes,
            partners = partners.await().map { partner ->
                partner.convertToModelV3(
                    fetchedJobs
                        .filter { it.partnerId == partner.id }
                        .map(JobEntity::convertToModel)
                )
            },
            activities = activities.await()
                .sortedBy { it.startTime }
                .map(ActivityEntity::convertToModel)
        )
    }
}
