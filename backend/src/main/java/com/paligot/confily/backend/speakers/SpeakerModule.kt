package com.paligot.confily.backend.speakers

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule.commonApi
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule.storage
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv.projectName

object SpeakerModule {
    val speakerDao = lazy { SpeakerDao(projectName, cloudFirestore.value, storage.value) }
    val speakerRepository =
        lazy { SpeakerRepository(commonApi.value, eventFirestore.value, speakerDao.value) }
}
