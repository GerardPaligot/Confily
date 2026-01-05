package com.paligot.confily.backend.export.infrastructure.factory

import com.paligot.confily.backend.export.application.ExportEventRepositoryExposed
import com.paligot.confily.backend.export.application.ExportPartnersRepositoryExposed
import com.paligot.confily.backend.export.application.ExportPlanningRepositoryExposed
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule

object ExportModule {
    val exportEventRepository by lazy {
        ExportEventRepositoryExposed(PostgresModule.database)
    }
    val exportPlanningRepository by lazy {
        ExportPlanningRepositoryExposed(PostgresModule.database)
    }
    val exportPartnersRepository by lazy {
        ExportPartnersRepositoryExposed(PostgresModule.database)
    }
}
