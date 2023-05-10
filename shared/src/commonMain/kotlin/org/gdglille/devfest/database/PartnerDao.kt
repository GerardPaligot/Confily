package org.gdglille.devfest.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import org.gdglille.devfest.Platform
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.JobUi
import org.gdglille.devfest.models.PartnerGroupUi
import org.gdglille.devfest.models.PartnerGroupsUi
import org.gdglille.devfest.models.PartnerItemUi
import org.gdglille.devfest.models.PartnerV2
import org.gdglille.devfest.models.SalaryUi

class PartnerDao(private val db: Conferences4HallDatabase, private val platform: Platform) {
    private val partnerMapper =
        { id: String, name: String, description: String, logoUrl: String, siteUrl: String?,
            twitterUrl: String?, twitterMessage: String?, linkedinUrl: String?,
            linkedinMessage: String?, formattedAddress: List<String>?, address: String?,
            latitude: Double?, longitude: Double? ->
            PartnerItemUi(
                id = id,
                name = name,
                description = description,
                logoUrl = logoUrl,
                siteUrl = siteUrl,
                twitterUrl = twitterUrl,
                twitterMessage = twitterMessage,
                linkedinUrl = linkedinUrl,
                linkedinMessage = linkedinMessage,
                formattedAddress = formattedAddress?.toImmutableList(),
                address = address,
                latitude = latitude,
                longitude = longitude,
                jobs = persistentListOf()
            )
        }
    private val jobsMapper =
        { url: String, partner_id: String, event_id: String, title: String, company_name: String,
            location: String, salary_min: Long?, salary_max: Long?, salary_recurrence: String?,
            requirements: Double, publish_date: Long, propulsed: String ->
            JobUi(
                url = url,
                title = title,
                companyName = company_name,
                location = location,
                salary = if (salary_min != null && salary_max != null && salary_recurrence != null) {
                    SalaryUi(
                        min = salary_min.toInt(),
                        max = salary_max.toInt(),
                        recurrence = salary_recurrence
                    )
                } else {
                    null
                },
                requirements = requirements.toInt(),
                propulsed = propulsed
            )
        }

    fun fetchPartners(eventId: String): Flow<PartnerGroupsUi> = db.transactionWithResult {
        return@transactionWithResult db.partnerQueries.selectPartnerTypes(eventId).asFlow()
            .mapToList().flatMapConcat { types ->
                return@flatMapConcat combine(
                    types.map { type ->
                        db.partnerQueries.selectPartners(eventId, type.name, partnerMapper)
                            .asFlow()
                            .mapToList()
                            .map { type.name to it.chunked(3).toImmutableList() }
                    },
                    transform = { results ->
                        PartnerGroupsUi(
                            groups = types.map { type ->
                                PartnerGroupUi(
                                    type = type.name,
                                    partners = results.find { it.first == type.name }!!.second
                                        .map { it.toImmutableList() }
                                        .toImmutableList()
                                )
                            }.toImmutableList()
                        )
                    }
                )
            }
    }

    fun fetchPartner(eventId: String, id: String): Flow<PartnerItemUi> =
        db.partnerQueries.selectPartner(eventId, id, partnerMapper).asFlow().mapToOne()
            .combine(
                db.partnerQueries.selectJobs(eventId, id, jobsMapper).asFlow().mapToList()
            ) { partner, jobs ->
                partner.copy(jobs = jobs.toImmutableList())
            }

    fun insertPartners(eventId: String, partners: Map<String, List<PartnerV2>>) = db.transaction {
        partners.keys.forEachIndexed { index, type ->
            db.partnerQueries.insertPartnerType(
                order_ = index.toLong(),
                name = type,
                event_id = eventId
            )
        }
        partners.entries.forEach { entry ->
            entry.value.forEach { partner ->
                db.partnerQueries.insertPartner(
                    id = partner.id,
                    name = partner.name,
                    description = partner.description,
                    event_id = eventId,
                    type_id = entry.key,
                    type = entry.key,
                    logo_url = if (platform.hasSupportSVG) {
                        partner.media.svg
                    } else if (partner.media.pngs != null) {
                        partner.media.pngs!!._250
                    } else {
                        partner.media.svg
                    },
                    site_url = partner.siteUrl,
                    twitter_url = partner.twitterUrl,
                    twitter_message = partner.twitterMessage,
                    linkedin_url = partner.linkedinUrl,
                    linkedin_message = partner.linkedinMessage,
                    formatted_address = partner.address?.formatted,
                    address = partner.address?.address,
                    latitude = partner.address?.lat,
                    longitude = partner.address?.lng
                )
                partner.jobs.forEach {
                    db.partnerQueries.insertJob(
                        url = it.url,
                        partner_id = partner.id,
                        event_id = eventId,
                        title = it.title,
                        company_name = it.companyName,
                        location = it.location,
                        salary_max = it.salary?.max?.toLong(),
                        salary_min = it.salary?.min?.toLong(),
                        salary_recurrence = it.salary?.recurrence,
                        requirements = it.requirements,
                        publish_date = it.publishDate,
                        propulsed = it.propulsed
                    )
                }
            }
        }
    }
}
