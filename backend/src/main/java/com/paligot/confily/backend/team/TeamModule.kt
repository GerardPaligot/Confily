package com.paligot.confily.backend.team

import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.internals.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.InternalModule.commonApi
import com.paligot.confily.backend.internals.InternalModule.storage
import com.paligot.confily.backend.internals.SystemEnv.projectName

object TeamModule {
    val teamDao = lazy { TeamDao(projectName, cloudFirestore.value, storage.value) }
    val teamRepository = lazy { TeamRepository(commonApi.value, eventDao.value, teamDao.value) }
}
