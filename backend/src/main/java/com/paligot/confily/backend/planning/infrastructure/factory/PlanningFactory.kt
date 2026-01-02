package com.paligot.confily.backend.planning.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.storage.StorageModule
import com.paligot.confily.backend.planning.application.PlanningRepositoryDefault
import com.paligot.confily.backend.planning.domain.PlanningRepository

object PlanningFactory {
    val planningRepository: Lazy<PlanningRepository> = lazy {
        PlanningRepositoryDefault(
            eventStorage = StorageModule.eventStorage.value
        )
    }
}
