package com.paligot.confily.backend.activities.infrastructure.factory

import com.paligot.confily.backend.activities.application.ActivityRepositoryDefault
import com.paligot.confily.backend.activities.application.ActivityRepositoryExposed
import com.paligot.confily.backend.activities.infrastructure.firestore.ActivityFirestore
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.partners.infrastructure.factory.PartnerModule

object ActivityModule {
    val activityFirestore = lazy { ActivityFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val activityRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            ActivityRepositoryExposed(PostgresModule.database)
        } else {
            ActivityRepositoryDefault(
                FirestoreModule.eventFirestore.value,
                PartnerModule.partnerFirestore.value,
                activityFirestore.value
            )
        }
    }
}
