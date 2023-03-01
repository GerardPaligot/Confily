package org.gdglille.devfest.backend.jobs

import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotAcceptableException
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.internals.network.welovedevs.WeLoveDevsApi
import org.gdglille.devfest.backend.partners.PartnerDao
import org.gdglille.devfest.backend.partners.cms4partners.Cms4PartnersDao

private const val MaxPartnerChar = 6

class JobRepository(
    private val api: WeLoveDevsApi,
    private val eventDao: EventDao,
    private val partnerDao: PartnerDao,
    private val cms4PartnersDao: Cms4PartnersDao,
    private val jobDao: JobDao
) {
    suspend fun importWld(eventId: String, apiKey: String) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        if (event.wldConfig == null) throw NotAcceptableException("Wld config not initialized")
        val cms4PartnerDbs = cms4PartnersDao.list(event.year).filter { it.wldId != null }
        val partners = partnerDao.getAll(eventId).filter { it.wldId != null }
        val companyIds = cms4PartnerDbs.map { it.wldId!! } + partners.map { it.wldId!! }
        if (companyIds.isEmpty()) throw NotFoundException("No partner has WLD config")
        val jobs = api.fetchPublicJobs(companyIds, event.wldConfig.appId, event.wldConfig.apiKey)
        val jobsDb = jobs.hits
            .filter { it.title.contains("spontaneous_application").not() }
            .map { hit ->
                val id = "${hit.companyId.substring(0..MaxPartnerChar)}-${hit.publishDate}"
                val partnerId = cms4PartnerDbs.find { it.wldId == hit.companyId }?.id
                    ?: partners.find { it.wldId == hit.companyId }?.id
                    ?: throw NotAcceptableException("Partner WLD ${hit.companyId} not found")
                hit.convertToDb(id, partnerId)
            }
        jobDao.resetJobs(eventId, jobsDb)
        return@coroutineScope jobsDb.map { it.convertToModel() }
    }
}
