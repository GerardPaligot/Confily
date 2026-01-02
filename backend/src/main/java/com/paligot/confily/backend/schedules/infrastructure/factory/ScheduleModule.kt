package com.paligot.confily.backend.schedules.infrastructure.factory

import com.paligot.confily.backend.formats.infrastructure.factory.FormatModule
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.GoogleServicesModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.schedules.application.ScheduleAdminRepositoryDefault
import com.paligot.confily.backend.schedules.application.ScheduleAdminRepositoryExposed
import com.paligot.confily.backend.schedules.application.ScheduleRepositoryDefault
import com.paligot.confily.backend.schedules.application.ScheduleRepositoryExposed
import com.paligot.confily.backend.schedules.infrastructure.firestore.ScheduleItemFirestore
import com.paligot.confily.backend.sessions.infrastructure.factory.SessionModule
import com.paligot.confily.backend.speakers.infrastructure.factory.SpeakerModule

object ScheduleModule {
    val scheduleItemFirestore = lazy {
        ScheduleItemFirestore(
            SystemEnv.projectName,
            GoogleServicesModule.cloudFirestore.value
        )
    }
    val scheduleRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            ScheduleRepositoryExposed(PostgresModule.database)
        } else {
            ScheduleRepositoryDefault(
                FirestoreModule.eventFirestore.value,
                SessionModule.sessionFirestore.value,
                FirestoreModule.categoryFirestore.value,
                FormatModule.formatFirestore.value,
                SpeakerModule.speakerFirestore.value,
                scheduleItemFirestore.value
            )
        }
    }
    val scheduleAdminRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            ScheduleAdminRepositoryExposed(PostgresModule.database)
        } else {
            ScheduleAdminRepositoryDefault(
                FirestoreModule.eventFirestore.value,
                SessionModule.sessionFirestore.value,
                FormatModule.formatFirestore.value,
                scheduleItemFirestore.value
            )
        }
    }
}
