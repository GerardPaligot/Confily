package com.paligot.confily.backend.qanda

import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.internals.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.SystemEnv.projectName

object QAndAModule {
    val qAndADao = lazy { QAndADao(projectName, cloudFirestore.value) }
    val qAndARepository = lazy { QAndARepository(eventDao.value, qAndADao.value) }
}
