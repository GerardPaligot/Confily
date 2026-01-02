package com.paligot.confily.backend.integrations.domain

import kotlinx.serialization.Serializable

@Serializable
data class Integration(
    val id: String,
    val provider: IntegrationProvider,
    val usage: IntegrationUsage
)
