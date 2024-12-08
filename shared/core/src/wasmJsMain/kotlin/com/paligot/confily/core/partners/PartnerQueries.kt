package com.paligot.confily.core.partners

import com.paligot.confily.core.combineAllSerializableScopedFlow
import com.paligot.confily.core.getAllSerializableScopedFlow
import com.paligot.confily.core.getSerializableScopedFlow
import com.paligot.confily.core.partners.PartnerQueries.Scopes.JOBS
import com.paligot.confily.core.partners.PartnerQueries.Scopes.PARTNERS
import com.paligot.confily.core.partners.PartnerQueries.Scopes.PARTNER_TYPES
import com.paligot.confily.core.putSerializableScoped
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class PartnerQueries(private val settings: ObservableSettings) {
    private object Scopes {
        const val PARTNERS = "partners"
        const val PARTNER_TYPES = "partnerTypes"
        const val JOBS = "jobs"
    }

    fun insertPartner(partner: PartnerDb) {
        settings.putSerializableScoped(PARTNERS, partner.id, partner)
    }

    fun selectPartners(eventId: String, name: String): Flow<List<PartnerDb>> = combine(
        flows = settings.getAllSerializableScopedFlow<PartnerDb>(PARTNERS),
        transform = { partners ->
            partners
                .filter { it.eventId == eventId && it.type == name }
                .sortedBy { it.name }
        }
    )

    fun selectPartner(eventId: String, id: String): Flow<PartnerDb> =
        settings.getSerializableScopedFlow(PARTNERS, id)

    fun insertPartnerType(partnerTypeDb: PartnerTypeDb) {
        settings.putSerializableScoped(PARTNER_TYPES, partnerTypeDb.name, partnerTypeDb)
    }

    fun selectPartnerTypes(eventId: String): Flow<List<PartnerTypeDb>> =
        settings.combineAllSerializableScopedFlow(PARTNER_TYPES) { it.eventId == eventId }

    fun insertJob(job: JobDb) {
        settings.putSerializableScoped(JOBS, job.url, job)
    }

    fun selectJobs(eventId: String, partnerId: String): Flow<List<JobDb>> = combine(
        flows = settings.getAllSerializableScopedFlow<JobDb>(JOBS),
        transform = { jobs ->
            jobs
                .filter { it.eventId == eventId && it.partnerId == partnerId }
                .sortedBy { it.publishDate }
        }
    )
}
