package com.paligot.confily.backend.team

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule.commonApi
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule.storage
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv.projectName

object TeamModule {
    val teamDao = lazy { TeamDao(projectName, cloudFirestore.value, storage.value) }
    val teamRepository = lazy { TeamRepository(commonApi.value, eventFirestore.value, teamDao.value) }
}
