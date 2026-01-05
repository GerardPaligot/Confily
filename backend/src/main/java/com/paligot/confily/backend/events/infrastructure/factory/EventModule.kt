package com.paligot.confily.backend.events.infrastructure.factory

import com.paligot.confily.backend.addresses.infrastructure.factory.GeocodeModule
import com.paligot.confily.backend.events.application.EventAdminRepositoryExposed
import com.paligot.confily.backend.events.application.EventRepositoryExposed
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule

object EventModule {
    val eventRepository by lazy {
        EventRepositoryExposed(PostgresModule.database, GeocodeModule.geocodeApi)
    }
    val eventAdminRepository by lazy {
        EventAdminRepositoryExposed(PostgresModule.database, GeocodeModule.geocodeApi)
    }
}
