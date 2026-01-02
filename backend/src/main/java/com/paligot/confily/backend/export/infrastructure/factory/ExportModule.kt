package com.paligot.confily.backend.export.infrastructure.factory

import com.paligot.confily.backend.activities.infrastructure.factory.ActivityModule
import com.paligot.confily.backend.export.application.ExportEventAdminRepositoryDefault
import com.paligot.confily.backend.export.application.ExportEventRepositoryDefault
import com.paligot.confily.backend.export.application.ExportEventRepositoryExposed
import com.paligot.confily.backend.export.application.ExportPartnersAdminRepositoryDefault
import com.paligot.confily.backend.export.application.ExportPartnersRepositoryDefault
import com.paligot.confily.backend.export.application.ExportPartnersRepositoryExposed
import com.paligot.confily.backend.export.application.ExportPlanningAdminRepositoryDefault
import com.paligot.confily.backend.export.application.ExportPlanningRepositoryDefault
import com.paligot.confily.backend.export.application.ExportPlanningRepositoryExposed
import com.paligot.confily.backend.formats.infrastructure.factory.FormatModule
import com.paligot.confily.backend.internals.infrastructure.exposed.PostgresModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.storage.StorageModule
import com.paligot.confily.backend.internals.infrastructure.system.SystemEnv
import com.paligot.confily.backend.map.infrastructure.factory.MapModule
import com.paligot.confily.backend.partners.infrastructure.factory.JobModule
import com.paligot.confily.backend.partners.infrastructure.factory.PartnerModule
import com.paligot.confily.backend.qanda.infrastructure.factory.QAndAModule
import com.paligot.confily.backend.schedules.infrastructure.factory.ScheduleModule
import com.paligot.confily.backend.sessions.infrastructure.factory.SessionModule
import com.paligot.confily.backend.speakers.infrastructure.factory.SpeakerModule
import com.paligot.confily.backend.tags.infrastructure.factory.TagModule
import com.paligot.confily.backend.team.infrastructure.factory.TeamModule

object ExportModule {
    val exportEventAdminRepository = lazy {
        ExportEventAdminRepositoryDefault(
            FirestoreModule.eventFirestore.value,
            StorageModule.eventStorage.value,
            QAndAModule.qAndAFirestore.value,
            TeamModule.teamFirestore.value,
            MapModule.mapFirestore.value,
            PartnerModule.partnerFirestore.value
        )
    }
    val exportEventRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            ExportEventRepositoryExposed(PostgresModule.database)
        } else {
            ExportEventRepositoryDefault(
                FirestoreModule.eventFirestore.value,
                StorageModule.eventStorage.value
            )
        }
    }
    val exportPlanningAdminRepository = lazy {
        ExportPlanningAdminRepositoryDefault(
            FirestoreModule.eventFirestore.value,
            StorageModule.eventStorage.value,
            SpeakerModule.speakerFirestore.value,
            SessionModule.sessionFirestore.value,
            FirestoreModule.categoryFirestore.value,
            FormatModule.formatFirestore.value,
            TagModule.tagFirestore.value,
            ScheduleModule.scheduleItemFirestore.value
        )
    }
    val exportPlanningRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            ExportPlanningRepositoryExposed(PostgresModule.database)
        } else {
            ExportPlanningRepositoryDefault(
                FirestoreModule.eventFirestore.value,
                StorageModule.eventStorage.value
            )
        }
    }
    val exportPartnersAdminRepository = lazy {
        ExportPartnersAdminRepositoryDefault(
            FirestoreModule.eventFirestore.value,
            StorageModule.eventStorage.value,
            PartnerModule.partnerFirestore.value,
            JobModule.jobFirestore.value,
            ActivityModule.activityFirestore.value
        )
    }
    val exportPartnersRepository = lazy {
        if (SystemEnv.DatabaseConfig.hasPostgres) {
            ExportPartnersRepositoryExposed(PostgresModule.database)
        } else {
            ExportPartnersRepositoryDefault(
                FirestoreModule.eventFirestore.value,
                StorageModule.eventStorage.value
            )
        }
    }
}
