package com.paligot.confily.backend.integrations.infrastructure.api

import com.paligot.confily.backend.integrations.domain.CreateIntegration
import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import io.ktor.server.plugins.NotFoundException
import kotlinx.serialization.KSerializer

interface IntegrationDeserializerRegistry {
    fun serializerFor(provider: IntegrationProvider): KSerializer<out CreateIntegration>
}

class DefaultIntegrationDeserializerRegistry : IntegrationDeserializerRegistry {
    private val serializers: Map<IntegrationProvider, KSerializer<out CreateIntegration>> = mapOf(
        IntegrationProvider.SLACK to CreateIntegration.CreateSlackIntegration.serializer(),
        IntegrationProvider.BILLETWEB to CreateIntegration.CreateBilletWebIntegration.serializer(),
        IntegrationProvider.OPENPLANNER to CreateIntegration.CreateOpenPlannerIntegration.serializer(),
        IntegrationProvider.WEBHOOK to CreateIntegration.CreateWebhookIntegration.serializer()
    )

    override fun serializerFor(provider: IntegrationProvider): KSerializer<out CreateIntegration> {
        return serializers[provider]
            ?: throw NotFoundException("No serializer found for provider: $provider")
    }
}
