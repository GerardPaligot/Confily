package com.paligot.confily.backend.speakers.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.speakers.application.SpeakerAdminRepositoryDefault
import com.paligot.confily.backend.speakers.application.SpeakerAdminRepositoryExposed
import com.paligot.confily.backend.speakers.application.SpeakerRepositoryDefault
import com.paligot.confily.backend.speakers.application.SpeakerRepositoryExposed
import com.paligot.confily.backend.speakers.infrastructure.firestore.SpeakerFirestore
import com.paligot.confily.backend.speakers.infrastructure.storage.SpeakerStorage

object SpeakerModule {
    val speakerFirestore = lazy { SpeakerFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val speakerStorage = lazy { SpeakerStorage(InternalModule.storage.value) }
    val speakerRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            SpeakerRepositoryExposed(PostgresModule.database)
        } else {
            SpeakerRepositoryDefault(speakerFirestore.value)
        }
    }
    val speakerAdminRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            SpeakerAdminRepositoryExposed(
                PostgresModule.database,
                InternalModule.commonApi.value,
                speakerStorage.value
            )
        } else {
            SpeakerAdminRepositoryDefault(
                InternalModule.commonApi.value,
                FirestoreModule.eventFirestore.value,
                speakerFirestore.value,
                speakerStorage.value
            )
        }
    }
}
