package com.paligot.confily.backend.team.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.team.application.TeamAdminRepositoryExposed
import com.paligot.confily.backend.team.application.TeamRepositoryExposed
import com.paligot.confily.backend.team.infrastructure.storage.TeamStorage

object TeamModule {
    val teamStorage by lazy {
        TeamStorage(InternalModule.storage)
    }
    val teamRepository by lazy {
        TeamRepositoryExposed(PostgresModule.database)
    }
    val teamAdminRepository by lazy {
        TeamAdminRepositoryExposed(
            PostgresModule.database,
            InternalModule.commonApi,
            teamStorage
        )
    }
}
