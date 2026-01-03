package com.paligot.confily.backend.integrations.domain

import kotlinx.serialization.SerialName

enum class IntegrationProvider {
    @SerialName("slack")
    SLACK,

    @SerialName("billetweb")
    BILLETWEB,

    @SerialName("openplanner")
    OPENPLANNER,

    @SerialName("partnersconnect")
    PARTNERSCONNECT,

    @SerialName("webhook")
    WEBHOOK
}
