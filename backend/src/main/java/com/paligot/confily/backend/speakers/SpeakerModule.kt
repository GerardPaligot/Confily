package com.paligot.confily.backend.speakers

import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.internals.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.InternalModule.commonApi
import com.paligot.confily.backend.internals.InternalModule.storage
import com.paligot.confily.backend.internals.SystemEnv.projectName

object SpeakerModule {
    val speakerDao = lazy { SpeakerDao(projectName, cloudFirestore.value, storage.value) }
    val speakerRepository =
        lazy { SpeakerRepository(commonApi.value, eventDao.value, speakerDao.value) }
}
