package com.paligot.confily.backend.integrations.application

import com.paligot.confily.backend.ConflictException
import com.paligot.confily.backend.events.infrastructure.exposed.EventEntity
import com.paligot.confily.backend.integrations.domain.CreateIntegration
import com.paligot.confily.backend.integrations.domain.Integration
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.integrations.domain.IntegrationRegistrar
import com.paligot.confily.backend.integrations.domain.IntegrationRepository
import com.paligot.confily.backend.integrations.domain.IntegrationUsage
import com.paligot.confily.backend.integrations.infrastructure.exposed.IntegrationEntity
import io.ktor.server.plugins.NotFoundException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class IntegrationRepositoryExposed(
    private val database: Database,
    private val registrars: List<IntegrationRegistrar<*>>
) : IntegrationRepository {
    override fun register(
        eventId: String,
        provider: IntegrationProvider,
        usage: IntegrationUsage,
        input: CreateIntegration
    ): String {
        val eventUuid = UUID.fromString(eventId)
        val registrar = registrars.find { it.supports(input) && usage in it.supportedUsages }
            ?: throw NotFoundException("No registrar found for input ${input::class.simpleName} and usage $usage")
        val existingIntegration = transaction(db = database) {
            IntegrationEntity.findIntegration(eventUuid, provider, usage)
        }
        if (existingIntegration != null) {
            val existingProvider = existingIntegration.provider
            throw ConflictException(
                "Integration with provider $existingProvider and usage $usage already exists for this event"
            )
        }
        @Suppress("UNCHECKED_CAST")
        return (registrar as IntegrationRegistrar<CreateIntegration>).register(eventId, usage, input)
    }

    override fun findByEvent(eventId: String): List<Integration> = transaction(db = database) {
        val event = EventEntity[UUID.fromString(eventId)]
        IntegrationEntity.findByEvent(event.id.value)
            .map {
                Integration(
                    id = it.id.value.toString(),
                    provider = it.provider,
                    usage = it.usage
                )
            }
    }

    override fun deleteById(eventId: String, integrationId: String) = transaction(db = database) {
        val integrationUuid = UUID.fromString(integrationId)
        val event = EventEntity[UUID.fromString(eventId)]

        // Find and delete the integration if it exists and belongs to this event
        val integrationEntity = IntegrationEntity.findByIntegrationId(event.id.value, integrationUuid)

        val registrars = registrars.filter { integrationEntity.usage in it.supportedUsages }
        for (registrar in registrars) {
            try {
                @Suppress("UNCHECKED_CAST")
                (registrar as IntegrationRegistrar<CreateIntegration>).unregister(integrationId)
            } catch (_: Exception) {
                // Log and continue to the next registrar
            }
        }
    }
}
