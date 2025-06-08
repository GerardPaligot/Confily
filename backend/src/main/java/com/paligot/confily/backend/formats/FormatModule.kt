package com.paligot.confily.backend.formats

import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv.projectName

object FormatModule {
    val formatDao = lazy { FormatDao(projectName, cloudFirestore.value) }
    val formatRepository = lazy { FormatRepository(formatDao.value) }
}
