package com.paligot.confily.backend.jobs

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv.projectName
import com.paligot.confily.backend.partners.PartnerModule.partnerDao
import com.paligot.confily.backend.third.parties.welovedevs.WeLoveDevsModule.wldApi

object JobModule {
    val jobDao = lazy { JobDao(projectName, cloudFirestore.value) }
    val jobRepository =
        lazy { JobRepository(wldApi.value, eventFirestore.value, partnerDao.value, jobDao.value) }
}
