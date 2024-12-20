package com.paligot.confily.core.partners

import com.paligot.confily.core.partners.entities.ActivityItem
import com.paligot.confily.core.partners.entities.JobItem
import com.paligot.confily.core.partners.entities.PartnerInfo
import com.paligot.confily.core.partners.entities.PartnerItem
import com.paligot.confily.core.partners.entities.PartnerType
import com.paligot.confily.models.PartnersActivities
import kotlinx.coroutines.flow.Flow

interface PartnerDao {
    fun fetchPartners(eventId: String): Flow<Map<PartnerType, List<PartnerItem>>>
    fun fetchPartner(eventId: String, id: String): Flow<PartnerInfo>
    fun fetchJobsByPartner(eventId: String, partnerId: String): Flow<List<JobItem>>
    fun fetchActivitiesByDay(eventId: String, day: String): Flow<List<ActivityItem>>
    fun fetchActivitiesByPartner(eventId: String, partnerId: String): Flow<List<ActivityItem>>
    fun insertPartners(eventId: String, partners: PartnersActivities)
}
