package com.paligot.confily.backend.speakers.infrastructure.factory

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.internals.infrastructure.firestore.SpeakerFirestore
import com.paligot.confily.backend.internals.infrastructure.storage.SpeakerStorage
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.speakers.application.SpeakerAdminRepositoryDefault
import com.paligot.confily.backend.speakers.application.SpeakerRepositoryDefault

object SpeakerModule {
    val speakerFirestore = lazy { SpeakerFirestore(SystemEnv.projectName, GoogleServicesModule.cloudFirestore.value) }
    val speakerStorage = lazy { SpeakerStorage(InternalModule.storage.value) }
    val speakerRepository = lazy { SpeakerRepositoryDefault(speakerFirestore.value) }
    val speakerAdminRepository = lazy {
        SpeakerAdminRepositoryDefault(
            InternalModule.commonApi.value,
            FirestoreModule.eventFirestore.value,
            speakerFirestore.value,
            speakerStorage.value
        )
    }
}
