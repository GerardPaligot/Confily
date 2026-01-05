package com.paligot.confily.backend.planning.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.planning.application.PlanningRepositoryExposed

object PlanningFactory {
    val planningRepository by lazy {
        PlanningRepositoryExposed(PostgresModule.database)
    }
}
