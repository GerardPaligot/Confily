package com.paligot.confily.backend.speakers.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.speakers.application.SpeakerAdminRepositoryExposed
import com.paligot.confily.backend.speakers.application.SpeakerRepositoryExposed
import com.paligot.confily.backend.speakers.infrastructure.storage.SpeakerStorage

object SpeakerModule {
    val speakerStorage by lazy { SpeakerStorage(InternalModule.storage) }
    val speakerRepository by lazy {
        SpeakerRepositoryExposed(PostgresModule.database)
    }
    val speakerAdminRepository by lazy {
        SpeakerAdminRepositoryExposed(
            PostgresModule.database,
            InternalModule.commonApi,
            speakerStorage
        )
    }
}
