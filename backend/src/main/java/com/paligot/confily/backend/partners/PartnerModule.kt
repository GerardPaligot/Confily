package com.paligot.confily.backend.partners

import com.paligot.confily.backend.activities.ActivityModule.activityDao
import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.internals.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.InternalModule.commonApi
import com.paligot.confily.backend.internals.InternalModule.storage
import com.paligot.confily.backend.internals.InternalModule.transcoder
import com.paligot.confily.backend.internals.SystemEnv.projectName
import com.paligot.confily.backend.jobs.JobModule.jobDao
import com.paligot.confily.backend.third.parties.geocode.GeocodeModule.geocodeApi

object PartnerModule {
    val partnerDao = lazy { PartnerDao(projectName, cloudFirestore.value, storage.value) }
    val partnerRepository = lazy {
        PartnerRepository(
            geocodeApi.value,
            commonApi.value,
            eventDao.value,
            partnerDao.value,
            activityDao.value,
            jobDao.value,
            transcoder.value
        )
    }
}
