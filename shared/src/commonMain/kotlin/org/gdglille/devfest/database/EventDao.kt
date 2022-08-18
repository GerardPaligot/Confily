package org.gdglille.devfest.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import org.gdglille.devfest.Image
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.models.Attendee
import org.gdglille.devfest.models.Event
import org.gdglille.devfest.models.EventInfoUi
import org.gdglille.devfest.models.EventUi
import org.gdglille.devfest.models.MenuItemUi
import org.gdglille.devfest.models.PartnerGroupsUi
import org.gdglille.devfest.models.PartnerItemUi
import org.gdglille.devfest.models.QuestionAndResponseActionUi
import org.gdglille.devfest.models.QuestionAndResponseUi
import org.gdglille.devfest.models.TicketInfoUi
import org.gdglille.devfest.models.TicketUi
import org.gdglille.devfest.toByteArray
import org.gdglille.devfest.toNativeImage

class EventDao(private val db: Conferences4HallDatabase, private val eventId: String) {
    private val eventMapper =
        { _: String, name: String, formattedAddress: List<String>, address: String, latitude: Double, longitude: Double,
            date: String, _: String, twitter: String?, twitter_url: String?, linkedin: String?, linkedin_url: String?,
            faq_url: String, coc_url: String, _: Long ->
            EventInfoUi(
                name = name,
                formattedAddress = formattedAddress,
                address = address,
                latitude = latitude,
                longitude = longitude,
                date = date,
                twitter = twitter,
                twitterUrl = twitter_url,
                linkedin = linkedin,
                linkedinUrl = linkedin_url,
                faqLink = faq_url,
                codeOfConductLink = coc_url
            )
        }

    private val partnerMapper =
        { name: String, _: String, _: String, logo_url: String, site_url: String? ->
            PartnerItemUi(logoUrl = logo_url, siteUrl = site_url, name = name)
        }

    private val menuMapper = { name: String, dish: String, accompaniment: String, dessert: String ->
        MenuItemUi(name = name, dish = dish, accompaniment = accompaniment, dessert = dessert)
    }

    private val ticketMapper =
        { _: String, ext_id: String?, _: String?, _: String?, firstname: String?, lastname: String?, _: String, qrcode: ByteArray ->
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
            .combineTransform(
                db.ticketQueries.selectTicket(eventId, ticketMapper).asFlow()
            ) { event, ticket ->
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

    fun fetchQAndA(): Flow<List<QuestionAndResponseUi>> = db.transactionWithResult {
        return@transactionWithResult combine(
            db.qAndAQueries.selectQAndA().asFlow().mapToList(),
            db.qAndAQueries.selectQAndAActions(eventId).asFlow().mapToList(),
            transform = { qAndADb, actionsDb ->
                qAndADb.map { qanda ->
                    QuestionAndResponseUi(
                        question = qanda.question,
                        response = qanda.response,
                        actions = actionsDb
                            .filter { it.qanda_id == qanda.order_ }
                            .sortedBy { it.order_ }
                            .map { QuestionAndResponseActionUi(label = it.label, url = it.url) }
                    )
                }
            }
        )
    }

    fun fetchMenus(): Flow<List<MenuItemUi>> =
        db.menuQueries.selectMenus(menuMapper).asFlow().mapToList()

    fun fetchCoC(): Flow<String> = db.eventQueries.selectCoc(eventId).asFlow().mapToOne()

    fun insertEvent(event: Event) = db.transaction {
        val eventDb = event.convertToModelDb()
        db.eventQueries.insertEvent(
            id = eventDb.id,
            name = eventDb.name,
            formatted_address = eventDb.formatted_address,
            address = eventDb.address,
            latitude = eventDb.latitude,
            longitude = eventDb.longitude,
            date = eventDb.date,
            coc = eventDb.coc,
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
            db.eventQueries.insertPartner(
                it.name,
                event.id,
                type = "silver",
                it.logoUrl,
                it.siteUrl
            )
        }
        event.partners.bronzes.forEach {
            db.eventQueries.insertPartner(
                it.name,
                event.id,
                type = "bronze",
                it.logoUrl,
                it.siteUrl
            )
        }
        event.partners.others.forEach {
            db.eventQueries.insertPartner(it.name, event.id, type = "other", it.logoUrl, it.siteUrl)
        }
        event.qanda.forEach { qAndA ->
            db.qAndAQueries.insertQAndA(qAndA.order.toLong(), eventId, qAndA.question, qAndA.response)
            qAndA.actions.forEach {
                db.qAndAQueries.insertQAndAAction(it.order.toLong(), eventId, qAndA.order.toLong(), it.label, it.url)
            }
        }
        event.menus.forEach {
            db.menuQueries.insertMenu(it.name, it.dish, it.accompaniment, it.dessert)
        }
        db.featuresActivatedQueries.insertFeatures(
            event_id = eventId,
            has_networking = event.features.hasNetworking,
            has_speaker_list = event.features.hasSpeakerList,
            has_partner_list = event.features.hasPartnerList,
            has_menus = event.features.hasMenus,
            has_qanda = event.features.hasQAndA,
            has_billet_web_ticket = event.features.hasBilletWebTicket
        )
    }

    fun updateTicket(qrCode: Image, barcode: String, attendee: Attendee?) =
        db.ticketQueries.insertUser(
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
