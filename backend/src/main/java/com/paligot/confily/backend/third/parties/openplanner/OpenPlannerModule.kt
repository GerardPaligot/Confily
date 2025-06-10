package com.paligot.confily.backend.third.parties.openplanner

import com.paligot.confily.backend.formats.infrastructure.factory.FormatModule.formatFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.categoryFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule.commonApi
import com.paligot.confily.backend.qanda.infrastructure.factory.QAndAModule.qAndAFirestore
import com.paligot.confily.backend.schedules.infrastructure.ScheduleModule.scheduleItemFirestore
import com.paligot.confily.backend.sessions.infrastructure.factory.SessionModule.sessionFirestore
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
            sessionFirestore.value,
            categoryFirestore.value,
            formatFirestore.value,
            scheduleItemFirestore.value,
            qAndAFirestore.value,
            teamDao.value
        )
    }
}
