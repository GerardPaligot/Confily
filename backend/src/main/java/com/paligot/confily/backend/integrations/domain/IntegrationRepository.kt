package com.paligot.confily.backend.integrations.domain

interface IntegrationRepository {
    fun register(
        eventId: String,
        provider: IntegrationProvider,
        usage: IntegrationUsage,
        input: CreateIntegration
    ): String

    fun findByEvent(eventId: String): List<Integration>

    fun deleteById(eventId: String, integrationId: String)
}
