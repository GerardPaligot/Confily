package com.paligot.confily.backend.sessions.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.sessions.application.SessionAdminVerbatimRepositoryExposed
import com.paligot.confily.backend.sessions.application.SessionVoteRepositoryExposed

object SessionModule {
    val sessionAdminVerbatimRepository by lazy {
        SessionAdminVerbatimRepositoryExposed(PostgresModule.database, InternalModule.driveDataSource)
    }
    val sessionVoteRepository by lazy {
        SessionVoteRepositoryExposed(PostgresModule.database)
    }
}
