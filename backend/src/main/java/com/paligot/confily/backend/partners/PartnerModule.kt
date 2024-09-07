package com.paligot.confily.backend.partners

import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.internals.InternalModule.database
import com.paligot.confily.backend.internals.InternalModule.storage
import com.paligot.confily.backend.internals.InternalModule.transcoder
import com.paligot.confily.backend.jobs.JobModule.jobDao
import com.paligot.confily.backend.third.parties.geocode.GeocodeModule.geocodeApi

object PartnerModule {
    val partnerDao = lazy { PartnerDao(database.value, storage.value) }
    val partnerRepository = lazy {
        PartnerRepository(
            geocodeApi.value,
            eventDao.value,
            partnerDao.value,
            jobDao.value,
            transcoder.value
        )
    }
}
