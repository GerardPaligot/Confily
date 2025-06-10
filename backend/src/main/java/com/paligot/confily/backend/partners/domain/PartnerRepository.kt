package com.paligot.confily.backend.partners.domain

import com.paligot.confily.models.PartnerV2
import com.paligot.confily.models.PartnersActivities

interface PartnerRepository {
    fun list(eventId: String): Map<String, List<PartnerV2>>
    suspend fun activities(eventId: String): PartnersActivities
}
