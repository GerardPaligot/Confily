package com.paligot.confily.core.partners

import com.paligot.confily.core.partners.entities.mapToUi
import com.paligot.confily.models.ui.PartnerUi
import com.paligot.confily.models.ui.PartnersActivitiesUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PartnerInteractor(
    private val repository: PartnerRepository
) {
    @NativeCoroutines
    fun partners(): Flow<PartnersActivitiesUi> = repository.partners()
        .map { it.mapToUi() }

    @NativeCoroutines
    fun partner(partnerId: String): Flow<PartnerUi> = repository.partner(partnerId)
        .map { it.mapToUi() }
}