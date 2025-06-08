package com.paligot.confily.backend.qanda

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv.projectName

object QAndAModule {
    val qAndADao = lazy { QAndADao(projectName, cloudFirestore.value) }
    val qAndARepository = lazy { QAndARepository(eventFirestore.value, qAndADao.value) }
}
