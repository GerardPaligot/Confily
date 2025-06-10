package com.paligot.confily.backend.export

import com.paligot.confily.backend.activities.infrastructure.factory.ActivityModule.activityFirestore
import com.paligot.confily.backend.formats.infrastructure.factory.FormatModule.formatFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.categoryFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.internals.infrastructure.factory.StorageModule.eventStorage
import com.paligot.confily.backend.map.infrastructure.factory.MapModule.mapFirestore
import com.paligot.confily.backend.partners.infrastructure.factory.PartnerModule.partnerFirestore
import com.paligot.confily.backend.qanda.infrastructure.factory.QAndAModule.qAndAFirestore
import com.paligot.confily.backend.schedules.infrastructure.ScheduleModule.scheduleItemFirestore
import com.paligot.confily.backend.sessions.SessionModule.sessionDao
import com.paligot.confily.backend.speakers.SpeakerModule.speakerDao
import com.paligot.confily.backend.tags.TagModule.tagDao
import com.paligot.confily.backend.team.TeamModule.teamDao
import com.paligot.confily.backend.third.parties.welovedevs.infrastructure.factory.JobModule.jobFirestore

object ExportModule {
    val exportEventRepository = lazy {
        ExportEventRepository(
            eventFirestore.value,
            eventStorage.value,
            qAndAFirestore.value,
            teamDao.value,
            mapFirestore.value,
            partnerFirestore.value
        )
    }
    val exportPlanningRepository = lazy {
        ExportPlanningRepository(
            eventFirestore.value,
            eventStorage.value,
            speakerDao.value,
            sessionDao.value,
            categoryFirestore.value,
            formatFirestore.value,
            tagDao.value,
            scheduleItemFirestore.value
        )
    }
    val exportPartnersRepository = lazy {
        ExportPartnersRepository(
            eventFirestore.value,
            eventStorage.value,
            partnerFirestore.value,
            jobFirestore.value,
            activityFirestore.value
        )
    }
}
