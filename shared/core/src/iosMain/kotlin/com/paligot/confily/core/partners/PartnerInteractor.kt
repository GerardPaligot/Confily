package com.paligot.confily.core.partners

import com.paligot.confily.core.partners.entities.mapToPartnerUi
import com.paligot.confily.core.partners.entities.mapToPartnersActivitiesUi
import com.paligot.confily.partners.ui.models.PartnerUi
import com.paligot.confily.partners.ui.models.PartnersActivitiesUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PartnerInteractor(
    private val repository: PartnerRepository
) {
    @NativeCoroutines
    fun partners(): Flow<PartnersActivitiesUi> = repository.partners()
        .map { it.mapToPartnersActivitiesUi() }

    @NativeCoroutines
    fun partner(partnerId: String): Flow<PartnerUi> = repository.partner(partnerId)
        .map { it.mapToPartnerUi() }
}
