package com.paligot.confily.core.partners

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.paligot.confily.core.partners.entities.ActivityItem
import com.paligot.confily.core.partners.entities.JobItem
import com.paligot.confily.core.partners.entities.PartnerInfo
import com.paligot.confily.core.partners.entities.PartnerItem
import com.paligot.confily.core.partners.entities.PartnerType
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.models.PartnersActivities
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlin.coroutines.CoroutineContext

class PartnerDaoSQLDelight(
    private val db: ConfilyDatabase,
    private val hasSvgSupport: Boolean,
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

    override fun insertPartners(eventId: String, partners: PartnersActivities) = db.transaction {
        partners.types.forEachIndexed { index, type ->
            db.partnerQueries.insertPartnerType(
                order_ = index.toLong(),
                name = type,
                event_id = eventId
            )
        }
        partners.partners.forEach { partner ->
            db.partnerQueries.insertPartner(
                id = partner.id,
                name = partner.name,
                description = partner.description,
                event_id = eventId,
                logo_url = if (hasSvgSupport) {
                    partner.media.svg
                } else if (partner.media.pngs != null) {
                    partner.media.pngs!!._250
                } else {
                    partner.media.svg
                },
                formatted_address = partner.address?.formatted,
                address = partner.address?.address,
                latitude = partner.address?.lat,
                longitude = partner.address?.lng
            )
            partner.socials.forEach { social ->
                db.socialQueries.insertSocial(
                    url = social.url,
                    type = social.type.name,
                    ext_id = partner.id,
                    event_id = eventId
                )
            }
            partner.types.forEach { type ->
                db.partnerQueries.insertPartnerAndType(
                    id = "${partner.id}-$type",
                    partner_id = partner.id,
                    sponsor_id = type,
                    event_id = eventId
                )
            }
            partner.jobs.forEach { job ->
                db.partnerQueries.insertJob(
                    url = job.url,
                    partner_id = partner.id,
                    event_id = eventId,
                    title = job.title,
                    company_name = job.companyName,
                    location = job.location,
                    salary_max = job.salary?.max?.toLong(),
                    salary_min = job.salary?.min?.toLong(),
                    salary_recurrence = job.salary?.recurrence,
                    requirements = job.requirements,
                    publish_date = job.publishDate,
                    propulsed = job.propulsed
                )
            }
        }
        partners.activities.forEach {
            db.partnerQueries.insertPartnerActivity(
                id = it.id,
                name = it.name,
                date = LocalDateTime.parse(it.startTime).date.format(LocalDate.Formats.ISO),
                start_time = it.startTime,
                end_time = it.endTime,
                partner_id = it.partnerId,
                event_id = eventId
            )
        }
    }
}
