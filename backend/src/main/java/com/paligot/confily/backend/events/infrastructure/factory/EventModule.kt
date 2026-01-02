package com.paligot.confily.backend.events.infrastructure.factory

import com.paligot.confily.backend.addresses.infrastructure.factory.GeocodeModule
import com.paligot.confily.backend.events.application.EventAdminRepositoryDefault
import com.paligot.confily.backend.events.application.EventAdminRepositoryExposed
import com.paligot.confily.backend.events.application.EventRepositoryDefault
import com.paligot.confily.backend.events.application.EventRepositoryExposed
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.storage.StorageModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv

object EventModule {
    val eventRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            EventRepositoryExposed(
                PostgresModule.database,
                GeocodeModule.geocodeApi.value
            )
        } else {
            EventRepositoryDefault(
                FirestoreModule.eventFirestore.value,
                StorageModule.eventStorage.value,
                GeocodeModule.geocodeApi.value
            )
        }
    }
    val eventAdminRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            EventAdminRepositoryExposed(
                PostgresModule.database,
                GeocodeModule.geocodeApi.value
            )
        } else {
            EventAdminRepositoryDefault(
                FirestoreModule.eventFirestore.value,
                GeocodeModule.geocodeApi.value
            )
        }
    }
}
