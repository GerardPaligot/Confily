package com.paligot.confily.partners.ui.models

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

data class PartnersActivitiesUi(
    val partners: PartnerGroupsUi,
    val activities: ImmutableMap<String, ImmutableList<ActivityUi>>
) {
    companion object {
        val fake = PartnersActivitiesUi(
            partners = PartnerGroupsUi.fake,
            activities = persistentMapOf(
                "10h00" to persistentListOf(ActivityUi.fake, ActivityUi.fake),
                "11h00" to persistentListOf(ActivityUi.fake, ActivityUi.fake)
            )
        )
    }
}
