package com.paligot.confily.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Activity(
    val id: String,
    val name: String,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String?,
    @SerialName("partner_id")
    val partnerId: String
)
