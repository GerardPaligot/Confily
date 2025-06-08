package com.paligot.confily.backend.partners

import com.paligot.confily.backend.activities.infrastructure.factory.ActivityModule.activityFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule.commonApi
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule.storage
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule.transcoder
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv.projectName
import com.paligot.confily.backend.jobs.JobModule.jobDao
import com.paligot.confily.backend.third.parties.geocode.GeocodeModule.geocodeApi

object PartnerModule {
    val partnerDao = lazy { PartnerDao(projectName, cloudFirestore.value, storage.value) }
    val partnerRepository = lazy {
        PartnerRepository(
            geocodeApi.value,
            commonApi.value,
            eventFirestore.value,
            partnerDao.value,
            activityFirestore.value,
            jobDao.value,
            transcoder.value
        )
    }
}
