package com.paligot.confily.backend.integrations.infrastructure.factory

import com.paligot.confily.backend.integrations.application.IntegrationRepositoryExposed
import com.paligot.confily.backend.integrations.infrastructure.api.DefaultIntegrationDeserializerRegistry
import com.paligot.confily.backend.integrations.infrastructure.exposed.BilletWebRegistrar
import com.paligot.confily.backend.integrations.infrastructure.exposed.OpenPlannerRegistrar
import com.paligot.confily.backend.integrations.infrastructure.exposed.SlackRegistrar
import com.paligot.confily.backend.integrations.infrastructure.exposed.WebhookRegistrar
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule

object IntegrationModule {
    val deserializer by lazy { DefaultIntegrationDeserializerRegistry() }

    val integrationRepository by lazy {
        IntegrationRepositoryExposed(
            database = PostgresModule.database,
            registrars = listOf(
                BilletWebRegistrar(PostgresModule.database),
                OpenPlannerRegistrar(PostgresModule.database),
                SlackRegistrar(PostgresModule.database),
                WebhookRegistrar(PostgresModule.database)
            )
        )
    }
}
