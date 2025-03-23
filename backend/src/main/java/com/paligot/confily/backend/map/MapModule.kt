package com.paligot.confily.backend.map

import com.paligot.confily.backend.internals.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.InternalModule.storage
import com.paligot.confily.backend.internals.SystemEnv.projectName

object MapModule {
    val mapDao = lazy { MapDao(projectName, cloudFirestore.value, storage.value) }
    val mapRepository = lazy { MapRepository(mapDao.value) }
}
