package com.paligot.confily.backend.schedules.infrastructure

import com.paligot.confily.backend.formats.infrastructure.factory.FormatModule.formatFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.categoryFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.ScheduleItemFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv.projectName
import com.paligot.confily.backend.schedules.application.ScheduleAdminRepositoryDefault
import com.paligot.confily.backend.schedules.application.ScheduleRepositoryDefault
import com.paligot.confily.backend.sessions.SessionModule.sessionDao
import com.paligot.confily.backend.speakers.SpeakerModule.speakerDao

object ScheduleModule {
    val scheduleItemFirestore = lazy { ScheduleItemFirestore(projectName, cloudFirestore.value) }
    val scheduleRepository = lazy {
        ScheduleRepositoryDefault(
            eventFirestore.value,
            sessionDao.value,
            categoryFirestore.value,
            formatFirestore.value,
            speakerDao.value,
            scheduleItemFirestore.value
        )
    }
    val scheduleAdminRepository = lazy {
        ScheduleAdminRepositoryDefault(
            eventFirestore.value,
            sessionDao.value,
            formatFirestore.value,
            scheduleItemFirestore.value
        )
    }
}
