package com.paligot.confily.backend.partners.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.partners.application.JobRepositoryDefault
import com.paligot.confily.backend.partners.infrastructure.firestore.JobFirestore

object JobModule {
    val jobFirestore = lazy { JobFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val jobRepository = lazy {
        JobRepositoryDefault(
            WeLoveDevsModule.wldApi.value,
            FirestoreModule.eventFirestore.value,
            PartnerModule.partnerFirestore.value,
            jobFirestore.value
        )
    }
}
