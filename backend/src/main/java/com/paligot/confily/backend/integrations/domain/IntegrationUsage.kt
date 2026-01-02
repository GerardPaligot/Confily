package com.paligot.confily.backend.integrations.domain

import kotlinx.serialization.SerialName

enum class IntegrationUsage {
    @SerialName("notification")
    NOTIFICATION,

    @SerialName("ticketing")
    TICKETING,

    @SerialName("agenda")
    AGENDA,

    @SerialName("webhook")
    WEBHOOK
}
