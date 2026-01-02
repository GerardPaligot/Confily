package com.paligot.confily.backend.integrations.domain

interface IntegrationRegistrar<T : CreateIntegration> {
    val supportedUsages: Set<IntegrationUsage>

    fun register(eventId: String, usage: IntegrationUsage, input: T): String

    fun unregister(integrationId: String)

    fun supports(input: CreateIntegration): Boolean
}
