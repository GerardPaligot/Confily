package org.gdglille.devfest.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.PartnerGroupUi
import org.gdglille.devfest.models.PartnerGroupsUi
import org.gdglille.devfest.models.PartnerItemUi
import org.gdglille.devfest.models.PartnerV2

class PartnerDao(private val db: Conferences4HallDatabase, private val eventId: String) {
    private val partnerMapper =
        { id: String, name: String, description: String, logoUrl: String, siteUrl: String?,
            twitterUrl: String?, twitterMessage: String?, linkedinUrl: String?, linkedinMessage: String?,
            formattedAddress: List<String>?, address: String?, latitude: Double?, longitude: Double? ->
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
                formattedAddress = formattedAddress,
                address = address,
                latitude = latitude,
                longitude = longitude
            )
        }

    fun fetchPartners(): Flow<PartnerGroupsUi> = db.transactionWithResult {
        return@transactionWithResult db.eventQueries.selectPartnerTypes().asFlow().mapToList().flatMapConcat { types ->
            return@flatMapConcat combine(
                types.map { type ->
                    db.eventQueries.selectPartners(eventId, type.name, partnerMapper).asFlow().mapToList()
                        .map { type.name to it.chunked(3) }
                },
                transform = { results ->
                    PartnerGroupsUi(
                        groups = types.map { type ->
                            PartnerGroupUi(
                                type = type.name,
                                partners = results.find { it.first == type.name }!!.second
                            )
                        }
                    )
                }
            )
        }
    }

    fun fetchPartner(id: String): Flow<PartnerItemUi> =
        db.eventQueries.selectPartner(id, partnerMapper).asFlow().mapToOne()

    fun insertPartners(partners: Map<String, List<PartnerV2>>) = db.transaction {
        partners.keys.forEachIndexed { index, type ->
            db.eventQueries.insertPartnerType(order_ = index.toLong(), name = type)
        }
        partners.entries.forEach { entry ->
            entry.value.forEach {
                db.eventQueries.insertPartner(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    event_id = eventId,
                    type_id = entry.key,
                    type = entry.key,
                    logo_url = it.logoUrl,
                    site_url = it.siteUrl,
                    twitter_url = it.twitterUrl,
                    twitter_message = it.twitterMessage,
                    linkedin_url = it.linkedinUrl,
                    linkedin_message = it.linkedinMessage,
                    formatted_address = it.address?.formatted,
                    address = it.address?.address,
                    latitude = it.address?.lat,
                    longitude = it.address?.lng
                )
            }
        }
    }
}
