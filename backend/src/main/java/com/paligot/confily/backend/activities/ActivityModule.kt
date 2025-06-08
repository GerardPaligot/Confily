package com.paligot.confily.backend.activities

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv.projectName
import com.paligot.confily.backend.partners.PartnerModule.partnerDao

object ActivityModule {
    val activityDao = lazy { ActivityDao(projectName, cloudFirestore.value) }
    val activityRepository = lazy {
        ActivityRepository(eventFirestore.value, partnerDao.value, activityDao.value)
    }
}
