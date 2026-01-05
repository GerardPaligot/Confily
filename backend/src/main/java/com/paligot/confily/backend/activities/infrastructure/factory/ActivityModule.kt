package com.paligot.confily.backend.activities.infrastructure.factory

import com.paligot.confily.backend.activities.application.ActivityRepositoryExposed
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule

object ActivityModule {
    val activityRepository by lazy {
        ActivityRepositoryExposed(PostgresModule.database)
    }
}
