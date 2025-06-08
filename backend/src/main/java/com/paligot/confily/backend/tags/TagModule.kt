package com.paligot.confily.backend.tags

import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv.projectName

object TagModule {
    val tagDao = lazy { TagDao(projectName, cloudFirestore.value) }
    val tagRepository = lazy { TagRepository(tagDao.value) }
}
