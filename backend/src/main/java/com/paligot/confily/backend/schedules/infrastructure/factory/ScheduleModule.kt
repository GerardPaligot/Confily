package com.paligot.confily.backend.schedules.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.schedules.application.ScheduleAdminRepositoryExposed
import com.paligot.confily.backend.schedules.application.ScheduleRepositoryExposed

object ScheduleModule {
    val scheduleRepository by lazy {
        ScheduleRepositoryExposed(PostgresModule.database)
    }
    val scheduleAdminRepository by lazy {
        ScheduleAdminRepositoryExposed(PostgresModule.database)
    }
}
