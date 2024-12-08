package com.paligot.confily.core.partners

import com.paligot.confily.core.agenda.convertToDb
import com.paligot.confily.core.partners.entities.ActivityItem
import com.paligot.confily.core.partners.entities.JobItem
import com.paligot.confily.core.partners.entities.PartnerInfo
import com.paligot.confily.core.partners.entities.PartnerItem
import com.paligot.confily.core.partners.entities.PartnerType
import com.paligot.confily.core.socials.SocialQueries
import com.paligot.confily.models.PartnersActivities
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

class PartnerDaoSettings(
    private val partnerQueries: PartnerQueries,
    private val socialQueries: SocialQueries,
    private val hasSvgSupport: Boolean
) : PartnerDao {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchPartners(eventId: String): Flow<Map<PartnerType, List<PartnerItem>>> =
        partnerQueries.selectPartnerTypes(eventId)
            .flatMapConcat { types ->
                combine(
                    flows = types.map { type ->
                        partnerQueries.selectPartners(eventId, name = type.name)
                            .map { partners -> partners.map { it.mapToEntity() } }
                            .map { type.mapToEntity() to it }
                    },
                    transform = { results ->
                        results.associate { it }
                    }
                )
            }

    override fun fetchPartner(eventId: String, id: String): Flow<PartnerInfo> =
        partnerQueries.selectPartner(eventId, id)
            .map { it.mapToInfoEntity() }

    override fun fetchJobsByPartner(eventId: String, partnerId: String): Flow<List<JobItem>> =
        partnerQueries.selectJobs(eventId, partnerId)
            .map { it.map { it.mapToEntity() } }

    override fun fetchActivitiesByDay(eventId: String, day: String): Flow<List<ActivityItem>> {
        TODO("Not yet implemented")
    }

    override fun fetchActivitiesByPartner(
        eventId: String,
        partnerId: String
    ): Flow<List<ActivityItem>> {
        TODO("Not yet implemented")
    }

    override fun insertPartners(eventId: String, partners: PartnersActivities) {
        partners.partners.forEach { partner ->
            partner.types.forEach { type ->
                partnerQueries.insertPartner(partner.convertToDb(eventId, type, hasSvgSupport))
            }
            partner.socials.forEach { social ->
                socialQueries.upsertSocial(social.convertToDb(eventId, partner.id))
            }
            partner.jobs.forEach { job ->
                partnerQueries.insertJob(job.convertToDb(eventId, partner.id))
            }
        }
    }
}
