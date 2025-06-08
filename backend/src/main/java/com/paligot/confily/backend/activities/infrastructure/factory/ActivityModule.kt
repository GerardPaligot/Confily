package com.paligot.confily.backend.activities.infrastructure.factory

import com.paligot.confily.backend.activities.application.ActivityRepositoryDefault
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.firestore.ActivityFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.partners.PartnerModule

object ActivityModule {
    val activityFirestore = lazy { ActivityFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val activityRepository = lazy {
        ActivityRepositoryDefault(
            FirestoreModule.eventFirestore.value,
            PartnerModule.partnerDao.value,
            activityFirestore.value
        )
    }
}
