package com.paligot.confily.backend.talks

import com.paligot.confily.backend.categories.CategoryModule.categoryDao
import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.formats.FormatModule.formatDao
import com.paligot.confily.backend.internals.InternalModule.driveDataSource
import com.paligot.confily.backend.sessions.SessionModule.sessionDao
import com.paligot.confily.backend.speakers.SpeakerModule.speakerDao

object TalkModule {
    val talkRepository = lazy {
        TalkRepository(
            eventDao.value,
            speakerDao.value,
            sessionDao.value,
            categoryDao.value,
            formatDao.value,
            driveDataSource.value
        )
    }
}
