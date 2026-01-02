package com.paligot.confily.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatedEvent(
    @SerialName("event_id")
    val eventId: String,
    @SerialName("api_key")
    val apiKey: String
)
