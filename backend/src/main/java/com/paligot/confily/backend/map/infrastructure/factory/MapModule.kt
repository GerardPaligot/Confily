package com.paligot.confily.backend.map.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.map.application.MapAdminRepositoryDefault
import com.paligot.confily.backend.map.application.MapAdminRepositoryExposed
import com.paligot.confily.backend.map.application.MapRepositoryDefault
import com.paligot.confily.backend.map.application.MapRepositoryExposed
import com.paligot.confily.backend.map.infrastructure.firestore.MapFirestore
import com.paligot.confily.backend.map.infrastructure.storage.MapStorage

object MapModule {
    val mapFirestore = lazy {
        MapFirestore(
            SystemEnv.projectName,
            GoogleServicesModule.cloudFirestore.value,
            InternalModule.storage.value
        )
    }
    val mapStorage = lazy { MapStorage(InternalModule.storage.value) }
    val mapRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            MapRepositoryExposed(PostgresModule.database)
        } else {
            MapRepositoryDefault(mapFirestore.value)
        }
    }
    val mapAdminRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            MapAdminRepositoryExposed(
                PostgresModule.database,
                mapStorage.value
            )
        } else {
            MapAdminRepositoryDefault(mapFirestore.value, mapStorage.value)
        }
    }
}
