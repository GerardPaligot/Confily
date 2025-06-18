package com.paligot.confily.backend.export.infrastructure.factory

import com.paligot.confily.backend.activities.infrastructure.factory.ActivityModule
import com.paligot.confily.backend.export.application.ExportEventAdminRepositoryDefault
import com.paligot.confily.backend.export.application.ExportEventRepositoryDefault
import com.paligot.confily.backend.export.application.ExportPartnersAdminRepositoryDefault
import com.paligot.confily.backend.export.application.ExportPartnersRepositoryDefault
import com.paligot.confily.backend.export.application.ExportPlanningAdminRepositoryDefault
import com.paligot.confily.backend.export.application.ExportPlanningRepositoryDefault
import com.paligot.confily.backend.formats.infrastructure.factory.FormatModule
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule
import com.paligot.confily.backend.internals.infrastructure.factory.StorageModule
import com.paligot.confily.backend.map.infrastructure.factory.MapModule
import com.paligot.confily.backend.partners.infrastructure.factory.PartnerModule
import com.paligot.confily.backend.qanda.infrastructure.factory.QAndAModule
import com.paligot.confily.backend.schedules.infrastructure.ScheduleModule
import com.paligot.confily.backend.sessions.infrastructure.factory.SessionModule
import com.paligot.confily.backend.speakers.infrastructure.factory.SpeakerModule
import com.paligot.confily.backend.tags.infrastructure.factory.TagModule
import com.paligot.confily.backend.team.infrastructure.factory.TeamModule
import com.paligot.confily.backend.third.parties.welovedevs.infrastructure.factory.JobModule

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
        ExportEventRepositoryDefault(
            FirestoreModule.eventFirestore.value,
            StorageModule.eventStorage.value
        )
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
        ExportPlanningRepositoryDefault(
            FirestoreModule.eventFirestore.value,
            StorageModule.eventStorage.value
        )
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
        ExportPartnersRepositoryDefault(
            FirestoreModule.eventFirestore.value,
            StorageModule.eventStorage.value
        )
    }
}
