package com.paligot.confily.backend.third.parties.openplanner.infrastructure.factory

import com.paligot.confily.backend.formats.infrastructure.factory.FormatModule
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.InternalModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.partners.infrastructure.factory.PartnerModule.partnerStorage
import com.paligot.confily.backend.qanda.infrastructure.factory.QAndAModule
import com.paligot.confily.backend.schedules.infrastructure.factory.ScheduleModule
import com.paligot.confily.backend.sessions.infrastructure.factory.SessionModule
import com.paligot.confily.backend.speakers.infrastructure.factory.SpeakerModule
import com.paligot.confily.backend.team.infrastructure.factory.TeamModule
import com.paligot.confily.backend.third.parties.openplanner.application.OpenPlannerRepositoryDefault
import com.paligot.confily.backend.third.parties.openplanner.application.OpenPlannerRepositoryExposed
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.provider.OpenPlannerApi

object OpenPlannerModule {
    val openPlannerApi = lazy { OpenPlannerApi.Factory.create(enableNetworkLogs = true) }
    val openPlannerRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            OpenPlannerRepositoryExposed(
                PostgresModule.database,
                openPlannerApi.value,
                InternalModule.commonApi.value,
                SpeakerModule.speakerStorage.value,
                TeamModule.teamStorage.value,
                partnerStorage.value,
                InternalModule.transcoder.value
            )
        } else {
            OpenPlannerRepositoryDefault(
                openPlannerApi.value,
                InternalModule.commonApi.value,
                FirestoreModule.eventFirestore.value,
                SpeakerModule.speakerFirestore.value,
                SpeakerModule.speakerStorage.value,
                SessionModule.sessionFirestore.value,
                FirestoreModule.categoryFirestore.value,
                FormatModule.formatFirestore.value,
                ScheduleModule.scheduleItemFirestore.value,
                QAndAModule.qAndAFirestore.value,
                TeamModule.teamFirestore.value,
                TeamModule.teamStorage.value
            )
        }
    }
}
