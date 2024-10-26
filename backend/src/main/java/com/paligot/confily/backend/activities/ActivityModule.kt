package com.paligot.confily.backend.activities

import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.internals.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.SystemEnv.projectName
import com.paligot.confily.backend.partners.PartnerModule.partnerDao

object ActivityModule {
    val activityDao = lazy { ActivityDao(projectName, cloudFirestore.value) }
    val activityRepository = lazy {
        ActivityRepository(eventDao.value, partnerDao.value, activityDao.value)
    }
}
