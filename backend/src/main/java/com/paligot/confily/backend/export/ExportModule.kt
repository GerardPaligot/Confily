package com.paligot.confily.backend.export

import com.paligot.confily.backend.activities.ActivityModule.activityDao
import com.paligot.confily.backend.categories.infrastructure.factory.CategoryModule.categoryDao
import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.formats.FormatModule.formatDao
import com.paligot.confily.backend.jobs.JobModule.jobDao
import com.paligot.confily.backend.map.MapModule.mapDao
import com.paligot.confily.backend.partners.PartnerModule.partnerDao
import com.paligot.confily.backend.qanda.QAndAModule.qAndADao
import com.paligot.confily.backend.schedules.ScheduleModule.scheduleItemDao
import com.paligot.confily.backend.sessions.SessionModule.sessionDao
import com.paligot.confily.backend.speakers.SpeakerModule.speakerDao
import com.paligot.confily.backend.tags.TagModule.tagDao
import com.paligot.confily.backend.team.TeamModule.teamDao

object ExportModule {
    val exportEventRepository = lazy {
        ExportEventRepository(
            eventDao.value,
            qAndADao.value,
            teamDao.value,
            mapDao.value,
            partnerDao.value
        )
    }
    val exportPlanningRepository = lazy {
        ExportPlanningRepository(
            eventDao.value,
            speakerDao.value,
            sessionDao.value,
            categoryDao.value,
            formatDao.value,
            tagDao.value,
            scheduleItemDao.value
        )
    }
    val exportPartnersRepository = lazy {
        ExportPartnersRepository(
            eventDao.value,
            partnerDao.value,
            jobDao.value,
            activityDao.value
        )
    }
}
