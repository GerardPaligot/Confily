package com.paligot.confily.core.partners

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.paligot.confily.core.events.entities.Social
import com.paligot.confily.core.partners.entities.ActivityItem
import com.paligot.confily.core.partners.entities.JobItem
import com.paligot.confily.core.partners.entities.PartnerInfo
import com.paligot.confily.core.partners.entities.PartnerItem
import com.paligot.confily.core.partners.entities.PartnerType
import com.paligot.confily.db.ConfilyDatabase
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

class PartnerDaoSQLDelight(
    private val db: ConfilyDatabase,
    private val dispatcher: CoroutineContext
) : PartnerDao {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun fetchPartners(eventId: String): Flow<Map<PartnerType, List<PartnerItem>>> =
        db.transactionWithResult {
            db.partnerQueries.selectPartnerTypes(eventId, partnerType)
                .asFlow()
                .mapToList(dispatcher)
                .flatMapConcat { types ->
                    combine(
                        types.map { type ->
                            db.partnerQueries
                                .selectPartners(
                                    event_id = eventId,
                                    name = type.name,
                                    mapper = partnerItemMapper
                                )
                                .asFlow()
                                .mapToList(dispatcher)
                                .map { type to it }
                        },
                        transform = { results ->
                            results.associate { it }
                        }
                    )
                }
        }

    override fun fetchPartner(eventId: String, id: String): Flow<PartnerInfo> = db.partnerQueries
        .selectPartner(eventId, id, partnerMapper)
        .asFlow()
        .mapToOne(dispatcher)

    override fun fetchJobsByPartner(eventId: String, partnerId: String): Flow<List<JobItem>> =
        db.partnerQueries.selectJobs(eventId, partnerId, jobsMapper)
            .asFlow()
            .mapToList(dispatcher)

    override fun fetchSocialsByPartner(eventId: String, partnerId: String): Flow<List<Social>> =
        db.partnerQueries.selectSocials(eventId, partnerId, socialMapper)
            .asFlow()
            .mapToList(dispatcher)

    override fun fetchActivitiesByDay(
        eventId: String,
        day: String
    ): Flow<List<ActivityItem>> = db.partnerQueries
        .selectPartnerActivities(eventId, day, activityMapper)
        .asFlow()
        .mapToList(dispatcher)

    override fun fetchActivitiesByPartner(
        eventId: String,
        partnerId: String
    ): Flow<List<ActivityItem>> = db.partnerQueries
        .selectPartnerActivitiesByPartner(eventId, partnerId, activityMapper)
        .asFlow()
        .mapToList(dispatcher)
        .map { it.toImmutableList() }
}
