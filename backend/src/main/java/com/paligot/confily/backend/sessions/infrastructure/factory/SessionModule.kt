package com.paligot.confily.backend.sessions.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.internals.infrastructure.firestore.SessionFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.sessions.application.SessionAdminRepositoryDefault
import com.paligot.confily.backend.sessions.application.SessionAdminVerbatimRepositoryDefault
import com.paligot.confily.backend.sessions.application.SessionRepositoryDefault
import com.paligot.confily.backend.speakers.infrastructure.factory.SpeakerModule
import com.paligot.confily.backend.third.parties.geocode.GeocodeModule

object SessionModule {
    val sessionFirestore = lazy { SessionFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val sessionRepository = lazy {
        SessionRepositoryDefault(FirestoreModule.eventFirestore.value, sessionFirestore.value)
    }
    val sessionAdminRepository = lazy {
        SessionAdminRepositoryDefault(
            FirestoreModule.eventFirestore.value,
            sessionFirestore.value,
            GeocodeModule.geocodeApi.value
        )
    }
    val sessionAdminVerbatimRepository = lazy {
        SessionAdminVerbatimRepositoryDefault(
            SpeakerModule.speakerFirestore.value,
            sessionFirestore.value,
            InternalModule.driveDataSource.value
        )
    }
}
