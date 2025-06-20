package com.paligot.confily.backend.schedules.infrastructure

import com.paligot.confily.backend.formats.infrastructure.factory.FormatModule.formatFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.categoryFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.ScheduleItemFirestore
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv.projectName
import com.paligot.confily.backend.schedules.application.ScheduleAdminRepositoryDefault
import com.paligot.confily.backend.schedules.application.ScheduleRepositoryDefault
import com.paligot.confily.backend.sessions.infrastructure.factory.SessionModule.sessionFirestore
import com.paligot.confily.backend.speakers.infrastructure.factory.SpeakerModule.speakerFirestore

object ScheduleModule {
    val scheduleItemFirestore = lazy { ScheduleItemFirestore(projectName, cloudFirestore.value) }
    val scheduleRepository = lazy {
        ScheduleRepositoryDefault(
            eventFirestore.value,
            sessionFirestore.value,
            categoryFirestore.value,
            formatFirestore.value,
            speakerFirestore.value,
            scheduleItemFirestore.value
        )
    }
    val scheduleAdminRepository = lazy {
        ScheduleAdminRepositoryDefault(
            eventFirestore.value,
            sessionFirestore.value,
            formatFirestore.value,
            scheduleItemFirestore.value
        )
    }
}
