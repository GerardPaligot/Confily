package com.paligot.confily.backend.sessions

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv.projectName
import com.paligot.confily.backend.third.parties.geocode.GeocodeModule.geocodeApi

object SessionModule {
    val sessionDao = lazy { SessionDao(projectName, cloudFirestore.value) }
    val sessionRepository =
        lazy { SessionRepository(geocodeApi.value, eventFirestore.value, sessionDao.value) }
}
