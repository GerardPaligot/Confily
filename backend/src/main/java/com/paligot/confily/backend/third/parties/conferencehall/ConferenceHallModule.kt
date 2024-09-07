package com.paligot.confily.backend.third.parties.conferencehall

import com.paligot.confily.backend.categories.CategoryModule.categoryDao
import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.formats.FormatModule.formatDao
import com.paligot.confily.backend.internals.InternalModule.commonApi
import com.paligot.confily.backend.sessions.SessionModule.sessionDao
import com.paligot.confily.backend.speakers.SpeakerModule.speakerDao

object ConferenceHallModule {
    val conferenceHallApi = lazy { ConferenceHallApi.Factory.create(enableNetworkLogs = true) }
    val conferenceHallRepository = lazy {
        ConferenceHallRepository(
            conferenceHallApi.value,
            commonApi.value,
            eventDao.value,
            speakerDao.value,
            sessionDao.value,
            categoryDao.value,
            formatDao.value
        )
    }
}
