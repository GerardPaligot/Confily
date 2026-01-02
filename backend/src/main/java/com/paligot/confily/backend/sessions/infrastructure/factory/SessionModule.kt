package com.paligot.confily.backend.sessions.infrastructure.factory

import com.paligot.confily.backend.addresses.infrastructure.factory.GeocodeModule
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.sessions.application.SessionAdminRepositoryDefault
import com.paligot.confily.backend.sessions.application.SessionAdminRepositoryExposed
import com.paligot.confily.backend.sessions.application.SessionAdminVerbatimRepositoryDefault
import com.paligot.confily.backend.sessions.application.SessionAdminVerbatimRepositoryExposed
import com.paligot.confily.backend.sessions.application.SessionRepositoryDefault
import com.paligot.confily.backend.sessions.application.SessionRepositoryExposed
import com.paligot.confily.backend.sessions.infrastructure.firestore.SessionFirestore
import com.paligot.confily.backend.speakers.infrastructure.factory.SpeakerModule

object SessionModule {
    val sessionFirestore = lazy { SessionFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val sessionRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            SessionRepositoryExposed(PostgresModule.database)
        } else {
            SessionRepositoryDefault(FirestoreModule.eventFirestore.value, sessionFirestore.value)
        }
    }
    val sessionAdminRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            SessionAdminRepositoryExposed(
                PostgresModule.database,
                GeocodeModule.geocodeApi.value
            )
        } else {
            SessionAdminRepositoryDefault(
                FirestoreModule.eventFirestore.value,
                sessionFirestore.value,
                GeocodeModule.geocodeApi.value
            )
        }
    }
    val sessionAdminVerbatimRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            SessionAdminVerbatimRepositoryExposed(
                PostgresModule.database,
                InternalModule.driveDataSource.value
            )
        } else {
            SessionAdminVerbatimRepositoryDefault(
                SpeakerModule.speakerFirestore.value,
                sessionFirestore.value,
                InternalModule.driveDataSource.value
            )
        }
    }
}
