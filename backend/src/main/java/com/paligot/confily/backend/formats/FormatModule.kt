package com.paligot.confily.backend.formats

import com.paligot.confily.backend.internals.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.SystemEnv.projectName

object FormatModule {
    val formatDao = lazy { FormatDao(projectName, cloudFirestore.value) }
    val formatRepository = lazy { FormatRepository(formatDao.value) }
}
