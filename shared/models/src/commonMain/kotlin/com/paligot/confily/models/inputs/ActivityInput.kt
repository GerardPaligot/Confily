package com.paligot.confily.models.inputs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityInput(
    val name: String,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String,
    @SerialName("partner_id")
    val partnerId: String
) : Validator {
    override fun validate(): List<String> = mutableListOf<String>().apply {
        if (name == "") add("Name can't be empty")
        if (startTime == "") add("Start time can't be empty")
        if (endTime == "") add("End time can't be empty")
        if (partnerId == "") add("Partner can't be empty")
    }
}
