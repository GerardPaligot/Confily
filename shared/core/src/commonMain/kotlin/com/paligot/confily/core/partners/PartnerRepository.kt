package com.paligot.confily.core.partners

import com.paligot.confily.core.database.EventDao
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
        fun create(eventDao: EventDao, partnerDao: PartnerDao): PartnerRepository =
            PartnerRepositoryImpl(eventDao, partnerDao)
    }
}

class PartnerRepositoryImpl(
    private val eventDao: EventDao,
    private val partnerDao: PartnerDao
) : PartnerRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun partners(): Flow<PartnerGroupsUi> = eventDao.fetchEventId()
        .flatMapConcat { partnerDao.fetchPartners(eventId = it) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun partner(id: String): Flow<PartnerItemUi> = eventDao.fetchEventId()
        .flatMapConcat { partnerDao.fetchPartner(eventId = it, id = id) }
}
