package com.paligot.confily.models.inputs

import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatingEventInput(
    val name: String,
    val year: String,
    val address: String,
    @SerialName("start_date")
    val startDate: String,
    @SerialName("end_date")
    val endDate: String,
    @SerialName("contact_phone")
    val contactPhone: String?,
    @SerialName("contact_email")
    val contactEmail: String,
    @SerialName("update_at")
    val updatedAt: Long = Clock.System.now().epochSeconds
) : Validator {
    override fun validate(): List<String> = emptyList()
}
