package com.paligot.confily.backend.integrations.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface CreateIntegration {
    @Serializable
    data class CreateSlackIntegration(val token: String, val channel: String) : CreateIntegration

    @Serializable
    class CreateBilletWebIntegration(
        val basic: String,
        @SerialName("event_id")
        val eventId: String
    ) : CreateIntegration

    @Serializable
    class CreateOpenPlannerIntegration(
        @SerialName("event_id")
        val eventId: String,
        @SerialName("api_key")
        val apiKey: String
    ) : CreateIntegration

    @Serializable
    class CreateWebhookIntegration(
        val url: String,
        @SerialName("health_url")
        val healthUrl: String? = null,
        @SerialName("header_auth")
        val headerAuth: String? = null
    ) : CreateIntegration
}
