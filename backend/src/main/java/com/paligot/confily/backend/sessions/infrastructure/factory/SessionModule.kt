package com.paligot.confily.backend.sessions.infrastructure.factory

import com.paligot.confily.backend.addresses.infrastructure.factory.GeocodeModule
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.sessions.application.SessionAdminRepositoryExposed
import com.paligot.confily.backend.sessions.application.SessionAdminVerbatimRepositoryExposed
import com.paligot.confily.backend.sessions.application.SessionRepositoryExposed

object SessionModule {
    val sessionRepository by lazy {
        SessionRepositoryExposed(PostgresModule.database)
    }
    val sessionAdminRepository by lazy {
        SessionAdminRepositoryExposed(PostgresModule.database, GeocodeModule.geocodeApi)
    }
    val sessionAdminVerbatimRepository by lazy {
        SessionAdminVerbatimRepositoryExposed(PostgresModule.database, InternalModule.driveDataSource)
    }
}
