package com.paligot.confily.backend.sessions

import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.internals.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.SystemEnv.projectName
import com.paligot.confily.backend.third.parties.geocode.GeocodeModule.geocodeApi

object SessionModule {
    val sessionDao = lazy { SessionDao(projectName, cloudFirestore.value) }
    val sessionRepository =
        lazy { SessionRepository(geocodeApi.value, eventDao.value, sessionDao.value) }
}
