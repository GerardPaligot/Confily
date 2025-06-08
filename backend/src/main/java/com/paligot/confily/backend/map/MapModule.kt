package com.paligot.confily.backend.map

import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule.storage
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv.projectName

object MapModule {
    val mapDao = lazy { MapDao(projectName, cloudFirestore.value, storage.value) }
    val mapRepository = lazy { MapRepository(mapDao.value) }
}
