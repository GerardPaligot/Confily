package org.gdglille.devfest.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatedEvent(
    val eventId: String,
    @SerialName("api_key")
    val apiKey: String
)
