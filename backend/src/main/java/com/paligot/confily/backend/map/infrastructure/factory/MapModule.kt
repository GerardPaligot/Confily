package com.paligot.confily.backend.map.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.map.application.MapAdminRepositoryExposed
import com.paligot.confily.backend.map.application.MapRepositoryExposed
import com.paligot.confily.backend.map.infrastructure.storage.MapStorage

object MapModule {
    val mapStorage by lazy { MapStorage(InternalModule.storage) }
    val mapRepository by lazy {
        MapRepositoryExposed(PostgresModule.database)
    }
    val mapAdminRepository by lazy {
        MapAdminRepositoryExposed(PostgresModule.database, mapStorage)
    }
}
