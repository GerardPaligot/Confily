package com.paligot.confily.core.partners

import com.paligot.confily.models.ui.ActivityUi
import com.paligot.confily.models.ui.PartnerGroupsUi
import com.paligot.confily.models.ui.PartnerItemUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.Flow

interface PartnerDao {
    fun fetchPartners(eventId: String): Flow<PartnerGroupsUi>
    fun fetchPartner(eventId: String, id: String): Flow<PartnerItemUi>
    fun fetchActivitiesByDay(eventId: String): Flow<ImmutableMap<String, ImmutableList<ActivityUi>>>

    fun fetchActivitiesByPartner(eventId: String, partnerId: String): Flow<ImmutableList<ActivityUi>>
}
