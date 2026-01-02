package com.paligot.confily.backend.partners.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.partners.application.JobRepositoryDefault
import com.paligot.confily.backend.partners.application.JobRepositoryExposed
import com.paligot.confily.backend.partners.infrastructure.firestore.JobFirestore

object JobModule {
    val jobFirestore = lazy { JobFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val jobRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            JobRepositoryExposed(
                PostgresModule.database
            )
        } else {
            JobRepositoryDefault(
                WeLoveDevsModule.wldApi.value,
                FirestoreModule.eventFirestore.value,
                PartnerModule.partnerFirestore.value,
                jobFirestore.value
            )
        }
    }
}
