package com.paligot.confily.core.partners

import com.paligot.confily.core.events.EventDao
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.partners.entities.Partner
import com.paligot.confily.core.partners.entities.Partners
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime

class PartnerRepositoryImpl(
    private val settings: ConferenceSettings,
    private val eventDao: EventDao,
    private val partnerDao: PartnerDao
) : PartnerRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun partners(): Flow<Partners> = settings.fetchEventId()
        .flatMapConcat {
            combine(
                flow = partnerDao.fetchPartners(eventId = it),
                flow2 = eventDao.fetchEvent(eventId = it)
                    .flatMapConcat { event ->
                        if (event == null) return@flatMapConcat flowOf(emptyList())
                        val now = Clock.System.now()
                            .toLocalDateTime(TimeZone.currentSystemDefault())
                            .date
                        val startInstant = event.startTime.date
                        val endInstant = event.endTime.date
                        val day = if (now < startInstant) {
                            startInstant.format(LocalDate.Formats.ISO)
                        } else if (now > endInstant) {
                            endInstant.format(LocalDate.Formats.ISO)
                        } else {
                            now.format(LocalDate.Formats.ISO)
                        }
                        partnerDao.fetchActivitiesByDay(eventId = it, day = day)
                    },
                transform = { partners, activities ->
                    Partners(groups = partners, activities = activities)
                }
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun partner(partnerId: String): Flow<Partner> = settings.fetchEventId()
        .flatMapConcat {
            combine(
                flow = partnerDao.fetchPartner(eventId = it, id = partnerId),
                flow2 = partnerDao.fetchJobsByPartner(eventId = it, partnerId = partnerId),
                flow3 = partnerDao.fetchSocialsByPartner(eventId = it, partnerId = partnerId),
                transform = { info, jobs, socials ->
                    Partner(info = info, jobs = jobs, socials = socials)
                }
            )
        }
}
