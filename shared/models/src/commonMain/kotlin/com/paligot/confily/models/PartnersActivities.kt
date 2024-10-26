package com.paligot.confily.models

import kotlinx.serialization.Serializable

@Serializable
data class PartnersActivities(
    val types: List<String>,
    val partners: List<PartnerV3>,
    val activities: List<Activity>
)
