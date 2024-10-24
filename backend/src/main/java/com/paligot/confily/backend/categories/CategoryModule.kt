package com.paligot.confily.backend.categories

import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.internals.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.SystemEnv.projectName

object CategoryModule {
    val categoryDao = lazy { CategoryDao(projectName, cloudFirestore.value) }
    val categoryRepository = lazy { CategoryRepository(eventDao.value, categoryDao.value) }
}
