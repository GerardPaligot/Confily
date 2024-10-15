package com.paligot.confily.core.partners

import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.models.ui.PartnerGroupsUi
import com.paligot.confily.models.ui.PartnerItemUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat

interface PartnerRepository {
    @NativeCoroutines
    fun partners(): Flow<PartnerGroupsUi>

    @NativeCoroutines
    fun partner(id: String): Flow<PartnerItemUi>

    object Factory {
        fun create(settings: ConferenceSettings, partnerDao: PartnerDao): PartnerRepository =
            PartnerRepositoryImpl(settings, partnerDao)
    }
}

class PartnerRepositoryImpl(
    private val settings: ConferenceSettings,
    private val partnerDao: PartnerDao
) : PartnerRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun partners(): Flow<PartnerGroupsUi> = settings.fetchEventId()
        .flatMapConcat { partnerDao.fetchPartners(eventId = it) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun partner(id: String): Flow<PartnerItemUi> = settings.fetchEventId()
        .flatMapConcat { partnerDao.fetchPartner(eventId = it, id = id) }
}
