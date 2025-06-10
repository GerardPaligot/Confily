package com.paligot.confily.backend.third.parties.welovedevs.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.firestore.JobFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.partners.PartnerModule
import com.paligot.confily.backend.third.parties.welovedevs.application.JobRepositoryDefault

object JobModule {
    val jobFirestore = lazy { JobFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val jobRepository =
        lazy {
            JobRepositoryDefault(
                WeLoveDevsModule.wldApi.value,
                FirestoreModule.eventFirestore.value,
                PartnerModule.partnerDao.value,
                jobFirestore.value
            )
        }
}
