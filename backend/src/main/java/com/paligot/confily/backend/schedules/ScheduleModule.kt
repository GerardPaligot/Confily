package com.paligot.confily.backend.schedules

import com.paligot.confily.backend.formats.infrastructure.factory.FormatModule.formatFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.categoryFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv.projectName
import com.paligot.confily.backend.sessions.SessionModule.sessionDao
import com.paligot.confily.backend.speakers.SpeakerModule.speakerDao

object ScheduleModule {
    val scheduleItemDao = lazy { ScheduleItemDao(projectName, cloudFirestore.value) }
    val scheduleRepository = lazy {
        ScheduleRepository(
            eventFirestore.value,
            sessionDao.value,
            categoryFirestore.value,
            formatFirestore.value,
            speakerDao.value,
            scheduleItemDao.value
        )
    }
}
