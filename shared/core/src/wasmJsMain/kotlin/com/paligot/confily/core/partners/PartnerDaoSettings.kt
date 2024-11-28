package com.paligot.confily.core.partners

import com.paligot.confily.core.events.entities.Social
import com.paligot.confily.core.partners.entities.ActivityItem
import com.paligot.confily.core.partners.entities.JobItem
import com.paligot.confily.core.partners.entities.PartnerInfo
import com.paligot.confily.core.partners.entities.PartnerItem
import com.paligot.confily.core.partners.entities.PartnerType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

class PartnerDaoSettings(
    private val partnerQueries: PartnerQueries
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

    override fun fetchSocialsByPartner(eventId: String, partnerId: String): Flow<List<Social>> =
        partnerQueries.selectSocials(eventId, partnerId)
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
}
