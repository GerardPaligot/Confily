package com.paligot.confily.core.partners

import com.paligot.confily.models.ui.PartnerGroupsUi
import com.paligot.confily.models.ui.PartnerItemUi
import kotlinx.coroutines.flow.Flow

interface PartnerDao {
    fun fetchPartners(eventId: String): Flow<PartnerGroupsUi>
    fun fetchPartner(eventId: String, id: String): Flow<PartnerItemUi>
}
