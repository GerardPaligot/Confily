package com.paligot.confily.core.partners

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.models.ui.ActivityUi
import com.paligot.confily.models.ui.PartnerGroupUi
import com.paligot.confily.models.ui.PartnerGroupsUi
import com.paligot.confily.models.ui.PartnerItemUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import kotlin.coroutines.CoroutineContext

class PartnerDaoSQLDelight(
    private val db: ConfilyDatabase,
    private val dispatcher: CoroutineContext
) : PartnerDao {
    override fun fetchPartners(eventId: String): Flow<PartnerGroupsUi> = db.transactionWithResult {
        return@transactionWithResult db.partnerQueries.selectPartnerTypes(eventId).asFlow()
            .mapToList(dispatcher).flatMapConcat { types ->
                return@flatMapConcat combine(
                    types.map { type ->
                        db.partnerQueries.selectPartners(
                            event_id = eventId,
                            name = type.name,
                            mapper = partnerMapper
                        ).asFlow().mapToList(dispatcher).map { type.name to it }
                    },
                    transform = { results ->
                        PartnerGroupsUi(
                            groups = types.map { type ->
                                PartnerGroupUi(
                                    type = type.name,
                                    partners = results.find { it.first == type.name }!!.second
                                        .toImmutableList()
                                )
                            }.toImmutableList()
                        )
                    }
                )
            }
    }

    override fun fetchPartner(eventId: String, id: String): Flow<PartnerItemUi> = combine(
        flow = db.partnerQueries
            .selectPartner(eventId, id, partnerMapper).asFlow().mapToOne(dispatcher),
        flow2 = db.partnerQueries.selectJobs(eventId, id, jobsMapper)
            .asFlow().mapToList(dispatcher),
        flow3 = db.partnerQueries.selectSocials(eventId, id, socialMapper)
            .asFlow().mapToList(dispatcher),
        transform = { partner, jobs, socials ->
            partner.copy(
                jobs = jobs.toImmutableList(),
                socials = socials.toImmutableList()
            )
        }
    )

    override fun fetchActivitiesByDay(
        eventId: String
    ): Flow<ImmutableMap<String, ImmutableList<ActivityUi>>> {
        val event = db.eventQueries.selectEvent(eventId).executeAsOne()
        val now = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val startInstant = LocalDateTime.parse(event.start_date).date
        val endInstant = LocalDateTime.parse(event.end_date).date
        val day = if (now < startInstant) {
            startInstant.format(LocalDate.Formats.ISO)
        } else if (now > endInstant) {
            endInstant.format(LocalDate.Formats.ISO)
        } else {
            now.format(LocalDate.Formats.ISO)
        }
        return db.partnerQueries.selectPartnerActivities(eventId, day, activityMapper)
            .asFlow().mapToList(dispatcher).map { activities ->
                activities
                    .groupBy { "${it.startTime.split(":")[0]}:00" }
                    .mapValues { it.value.toImmutableList() }
                    .toImmutableMap()
            }
    }

    override fun fetchActivitiesByPartner(
        eventId: String,
        partnerId: String
    ): Flow<ImmutableList<ActivityUi>> =
        db.partnerQueries.selectPartnerActivitiesByPartner(eventId, partnerId, activityMapper)
            .asFlow().mapToList(dispatcher).map { it.toImmutableList() }
}
