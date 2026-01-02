package com.paligot.confily.backend.partners.application

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.partners.domain.JobRepository
import com.paligot.confily.backend.partners.infrastructure.firestore.JobFirestore
import com.paligot.confily.backend.partners.infrastructure.firestore.PartnerFirestore
import com.paligot.confily.backend.partners.infrastructure.firestore.convertToEntity
import com.paligot.confily.backend.partners.infrastructure.firestore.convertToModel
import com.paligot.confily.backend.partners.infrastructure.provider.WeLoveDevsApi
import com.paligot.confily.models.Job

private const val MaxPartnerChar = 6

class JobRepositoryDefault(
    private val api: WeLoveDevsApi,
    private val eventDao: EventFirestore,
    private val partnerFirestore: PartnerFirestore,
    private val jobFirestore: JobFirestore
) : JobRepository {
    override suspend fun import(eventId: String): List<Job> {
        val event = eventDao.get(eventId)
        if (event.wldConfig == null) throw NotAcceptableException("Wld config not initialized")
        val partners = partnerFirestore.getAll(eventId).filter { it.wldId != null }
        val companyIds = partners.map { it.wldId!! }.filter { it.isNotEmpty() }
        if (companyIds.isEmpty()) throw NotFoundException("No partner has WLD config")
        val jobs = api.fetchPublicJobs(companyIds, event.wldConfig.appId, event.wldConfig.apiKey)
        val jobsDb = jobs.hits
            .filter { it.title.contains("spontaneous_application").not() }
            .map { hit ->
                val id = if (hit.companyId.length < MaxPartnerChar) {
                    hit.companyId
                } else {
                    "${hit.companyId.substring(0..MaxPartnerChar)}-${hit.publishDate}"
                }
                val partnerId = partners.find { it.wldId == hit.companyId }?.id
                    ?: throw NotAcceptableException("Partner WLD ${hit.companyId} not found")
                hit.convertToEntity(id, partnerId)
            }
        jobFirestore.resetJobs(eventId, jobsDb)
        return jobsDb.map { it.convertToModel() }
    }
}
