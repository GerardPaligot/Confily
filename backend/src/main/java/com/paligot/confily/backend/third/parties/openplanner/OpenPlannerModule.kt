package com.paligot.confily.backend.third.parties.openplanner

import com.paligot.confily.backend.formats.FormatModule.formatDao
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.categoryFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule.commonApi
import com.paligot.confily.backend.qanda.QAndAModule.qAndADao
import com.paligot.confily.backend.schedules.ScheduleModule.scheduleItemDao
import com.paligot.confily.backend.sessions.SessionModule.sessionDao
import com.paligot.confily.backend.speakers.SpeakerModule.speakerDao
import com.paligot.confily.backend.team.TeamModule.teamDao

object OpenPlannerModule {
    val openPlannerApi = lazy { OpenPlannerApi.Factory.create(enableNetworkLogs = true) }
    val openPlannerRepository = lazy {
        OpenPlannerRepository(
            openPlannerApi.value,
            commonApi.value,
            eventFirestore.value,
            speakerDao.value,
            sessionDao.value,
            categoryFirestore.value,
            formatDao.value,
            scheduleItemDao.value,
            qAndADao.value,
            teamDao.value
        )
    }
}
