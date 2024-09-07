package com.paligot.confily.backend.speakers

import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.internals.InternalModule.commonApi
import com.paligot.confily.backend.internals.InternalModule.database
import com.paligot.confily.backend.internals.InternalModule.storage

object SpeakerModule {
    val speakerDao = lazy { SpeakerDao(database.value, storage.value) }
    val speakerRepository =
        lazy { SpeakerRepository(commonApi.value, eventDao.value, speakerDao.value) }
}
