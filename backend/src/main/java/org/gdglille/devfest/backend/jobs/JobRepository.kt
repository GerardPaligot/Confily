package org.gdglille.devfest.backend.jobs

import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotAcceptableException
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.partners.PartnerDao
import org.gdglille.devfest.backend.third.parties.welovedevs.WeLoveDevsApi
import org.gdglille.devfest.backend.third.parties.welovedevs.convertToDb
import org.gdglille.devfest.backend.third.parties.welovedevs.convertToModel

private const val MaxPartnerChar = 6

class JobRepository(
    private val api: WeLoveDevsApi,
    private val eventDao: EventDao,
    private val partnerDao: PartnerDao,
    private val jobDao: JobDao
) {
    suspend fun importWld(eventId: String, apiKey: String) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        if (event.wldConfig == null) throw NotAcceptableException("Wld config not initialized")
        val partners = partnerDao.getAll(eventId).filter { it.wldId != null }
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
                hit.convertToDb(id, partnerId)
            }
        jobDao.resetJobs(eventId, jobsDb)
        return@coroutineScope jobsDb.map { it.convertToModel() }
    }
}
