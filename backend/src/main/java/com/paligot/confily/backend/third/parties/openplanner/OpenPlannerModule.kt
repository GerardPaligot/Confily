package com.paligot.confily.backend.third.parties.openplanner

import com.paligot.confily.backend.categories.CategoryModule.categoryDao
import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.formats.FormatModule.formatDao
import com.paligot.confily.backend.internals.InternalModule.commonApi
import com.paligot.confily.backend.qanda.QAndAModule.qAndADao
import com.paligot.confily.backend.schedules.ScheduleModule.scheduleItemDao
import com.paligot.confily.backend.sessions.SessionModule.sessionDao
import com.paligot.confily.backend.speakers.SpeakerModule.speakerDao

object OpenPlannerModule {
    val openPlannerApi = lazy { OpenPlannerApi.Factory.create(enableNetworkLogs = true) }
    val openPlannerRepository = lazy {
        OpenPlannerRepository(
            openPlannerApi.value,
            commonApi.value,
            eventDao.value,
            speakerDao.value,
            sessionDao.value,
            categoryDao.value,
            formatDao.value,
            scheduleItemDao.value,
            qAndADao.value
        )
    }
}
