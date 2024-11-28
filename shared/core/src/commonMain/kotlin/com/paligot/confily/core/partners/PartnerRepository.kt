package com.paligot.confily.core.partners

import com.paligot.confily.core.events.EventDao
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.partners.entities.Partner
import com.paligot.confily.core.partners.entities.Partners
import kotlinx.coroutines.flow.Flow

interface PartnerRepository {
    fun partners(): Flow<Partners>
    fun partner(partnerId: String): Flow<Partner>

    object Factory {
        fun create(
            settings: ConferenceSettings,
            eventDao: EventDao,
            partnerDao: PartnerDao
        ): PartnerRepository = PartnerRepositoryImpl(settings, eventDao, partnerDao)
    }
}
