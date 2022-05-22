package org.gdglille.devfest.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import org.gdglille.devfest.Image
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.Attendee
import org.gdglille.devfest.models.Event
import org.gdglille.devfest.models.EventInfoUi
import org.gdglille.devfest.models.EventUi
import org.gdglille.devfest.models.PartnerGroupsUi
import org.gdglille.devfest.models.PartnerItemUi
import org.gdglille.devfest.models.TicketInfoUi
import org.gdglille.devfest.models.TicketUi
import org.gdglille.devfest.toByteArray
import org.gdglille.devfest.toNativeImage

class EventDao(private val db: Conferences4HallDatabase, private val eventId: String) {
    private val eventMapper = { _: String, name: String, address: String, date: String, twitter: String?,
                                twitter_url: String?, linkedin: String?, linkedin_url: String?, faq_url: String,
                                coc_url: String, _: Long ->
        EventInfoUi(
            name = name,
            address = address,
            date = date,
            twitter = twitter,
            twitterUrl = twitter_url,
            linkedin = linkedin,
            linkedinUrl = linkedin_url,
            faqLink = faq_url,
            codeOfConductLink = coc_url
        )
    }

    private val partnerMapper = { name: String, _: String, _: String, logo_url: String, site_url: String? ->
        PartnerItemUi(logoUrl = logo_url, siteUrl = site_url, name = name)
    }

    private val ticketMapper = { _: String, ext_id: String?, _: String?, _: String?, firstname: String?, lastname: String?, _: String, qrcode: ByteArray ->
        TicketUi(
            info = if (ext_id != null && firstname != null && lastname != null) {
                TicketInfoUi(
                    id = ext_id,
                    firstName = firstname,
                    lastName = lastname
                )
            } else null,
            qrCode = qrcode.toNativeImage()
        )
    }

    fun fetchEvent(): Flow<EventUi> = db.transactionWithResult {
        return@transactionWithResult db.eventQueries.selectEvent(eventId, eventMapper).asFlow()
            .combineTransform(db.ticketQueries.selectTicket(eventId, ticketMapper).asFlow()) { event, ticket ->
                val eventInfo = event.executeAsOneOrNull() ?: return@combineTransform
                emit(EventUi(eventInfo = eventInfo, ticket = ticket.executeAsOneOrNull()))
            }
    }

    fun fetchPartners(): Flow<PartnerGroupsUi> = db.transactionWithResult {
        return@transactionWithResult combine(
            db.eventQueries.selectPartners("gold", eventId, partnerMapper).asFlow().mapToList(),
            db.eventQueries.selectPartners("silver", eventId, partnerMapper).asFlow().mapToList(),
            db.eventQueries.selectPartners("bronze", eventId, partnerMapper).asFlow().mapToList(),
            db.eventQueries.selectPartners("other", eventId, partnerMapper).asFlow().mapToList(),
            transform = { golds, silvers, bronzes, others ->
                PartnerGroupsUi(
                    golds = golds.chunked(3),
                    silvers = silvers.chunked(3),
                    bronzes = bronzes.chunked(3),
                    others = others.chunked(3)
                )
            }
        )
    }

    fun insertEvent(event: Event) = db.transaction {
        val eventDb = event.convertToModelDb()
        db.eventQueries.insertEvent(
            id = eventDb.id,
            name = eventDb.name,
            address = eventDb.address,
            date = eventDb.date,
            twitter = eventDb.twitter,
            twitter_url = eventDb.twitter_url,
            linkedin = eventDb.linkedin,
            linkedin_url = eventDb.linkedin_url,
            faq_url = eventDb.faq_url,
            coc_url = eventDb.coc_url,
            updated_at = eventDb.updated_at
        )
        event.partners.golds.forEach {
            db.eventQueries.insertPartner(it.name, event.id, type = "gold", it.logoUrl, it.siteUrl)
        }
        event.partners.silvers.forEach {
            db.eventQueries.insertPartner(it.name, event.id, type = "silver", it.logoUrl, it.siteUrl)
        }
        event.partners.bronzes.forEach {
            db.eventQueries.insertPartner(it.name, event.id, type = "bronze", it.logoUrl, it.siteUrl)
        }
        event.partners.others.forEach {
            db.eventQueries.insertPartner(it.name, event.id, type = "other", it.logoUrl, it.siteUrl)
        }
    }

    fun updateTicket(qrCode: Image, barcode: String, attendee: Attendee?) = db.ticketQueries.insertUser(
        id = attendee?.id,
        ext_id = attendee?.idExt,
        event_id = eventId,
        email = attendee?.email,
        firstname = attendee?.firstname,
        lastname = attendee?.name,
        barcode = barcode,
        qrcode = qrCode.toByteArray()
    )
}