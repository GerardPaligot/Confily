package com.paligot.confily.backend.events

import com.paligot.confily.backend.categories.CategoryModule.categoryDao
import com.paligot.confily.backend.formats.FormatModule.formatDao
import com.paligot.confily.backend.internals.GoogleServicesModule.cloudFirestore
import com.paligot.confily.backend.internals.InternalModule
import com.paligot.confily.backend.internals.SystemEnv.projectName
import com.paligot.confily.backend.partners.PartnerModule.partnerDao
import com.paligot.confily.backend.qanda.QAndAModule.qAndADao
import com.paligot.confily.backend.schedules.ScheduleModule.scheduleItemDao
import com.paligot.confily.backend.sessions.SessionModule.sessionDao
import com.paligot.confily.backend.speakers.SpeakerModule.speakerDao
import com.paligot.confily.backend.tags.TagModule.tagDao
import com.paligot.confily.backend.third.parties.geocode.GeocodeModule.geocodeApi

object EventModule {
    val eventDao = lazy {
        EventDao(projectName, cloudFirestore.value, InternalModule.storage.value)
    }
    val eventRepository = lazy {
        EventRepository(
            geocodeApi.value,
            eventDao.value,
            speakerDao.value,
            qAndADao.value,
            sessionDao.value,
            categoryDao.value,
            formatDao.value,
            scheduleItemDao.value,
            partnerDao.value
        )
    }
    val eventRepositoryV2 = lazy {
        EventRepositoryV2(
            eventDao.value,
            speakerDao.value,
            sessionDao.value,
            categoryDao.value,
            formatDao.value,
            scheduleItemDao.value,
            partnerDao.value,
            qAndADao.value
        )
    }
    val eventRepositoryV3 = lazy {
        EventRepositoryV3(
            eventDao.value,
            speakerDao.value,
            sessionDao.value,
            categoryDao.value,
            formatDao.value,
            scheduleItemDao.value,
            partnerDao.value,
            qAndADao.value
        )
    }
    val eventRepositoryV4 = lazy {
        EventRepositoryV4(
            eventDao.value,
            speakerDao.value,
            sessionDao.value,
            categoryDao.value,
            formatDao.value,
            tagDao.value,
            scheduleItemDao.value,
            partnerDao.value,
            qAndADao.value
        )
    }
}
