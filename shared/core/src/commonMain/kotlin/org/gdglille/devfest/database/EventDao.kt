package org.gdglille.devfest.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.paligot.confily.models.Attendee
import com.paligot.confily.models.EventItemList
import com.paligot.confily.models.EventV3
import com.paligot.confily.models.QuestionAndResponse
import com.paligot.confily.models.ui.EventItemUi
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.gdglille.devfest.db.Conferences4HallDatabase
import org.gdglille.devfest.exceptions.EventSavedException
import org.gdglille.devfest.models.ui.CoCUi
import org.gdglille.devfest.models.ui.EventInfoUi
import org.gdglille.devfest.models.ui.EventItemListUi
import org.gdglille.devfest.models.ui.EventUi
import org.gdglille.devfest.models.ui.Image
import org.gdglille.devfest.models.ui.MenuItemUi
import org.gdglille.devfest.models.ui.QuestionAndResponseActionUi
import org.gdglille.devfest.models.ui.QuestionAndResponseUi
import org.gdglille.devfest.models.ui.TicketInfoUi
import org.gdglille.devfest.models.ui.TicketUi
import org.gdglille.devfest.toByteArray
import org.gdglille.devfest.toNativeImage
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalSettingsApi::class)
class EventDao(
    private val db: Conferences4HallDatabase,
    private val settings: ObservableSettings,
    private val dispatcher: CoroutineContext
) {
    private val eventMapper =
        { _: String, name: String, formattedAddress: List<String>, address: String, latitude: Double, longitude: Double,
            date: String, _: String, _: String, _: String?, twitter: String?, twitterUrl: String?,
            linkedin: String?, linkedinUrl: String?, faqUrl: String, cocUrl: String, _: Long ->
            EventInfoUi(
                name = name,
                formattedAddress = formattedAddress.toImmutableList(),
                address = address,
                latitude = latitude,
                longitude = longitude,
                date = date,
                twitter = twitter,
                twitterUrl = twitterUrl,
                linkedin = linkedin,
                linkedinUrl = linkedinUrl,
                faqLink = faqUrl,
                codeOfConductLink = cocUrl
            )
        }

    private val eventItemMapper = { id: String, name: String, date: String, _: Long, _: Boolean ->
        com.paligot.confily.models.ui.EventItemUi(id = id, name = name, date = date)
    }

    private val menuMapper = { name: String, dish: String, accompaniment: String, dessert: String ->
        MenuItemUi(name = name, dish = dish, accompaniment = accompaniment, dessert = dessert)
    }

    private val cocMapper = { coc: String, email: String, phone: String? ->
        CoCUi(text = coc, phone = phone, email = email)
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
                } else {
                    null
                },
                qrCode = qrcode.toNativeImage()
            )
        }

    fun fetchEventList(): Flow<EventItemListUi> = combine(
        db.eventQueries.selectEventItem(true, eventItemMapper).asFlow().mapToList(dispatcher),
        db.eventQueries.selectEventItem(false, eventItemMapper).asFlow().mapToList(dispatcher),
        transform = { past, future ->
            EventItemListUi(future = future.toImmutableList(), past = past.toImmutableList())
        }
    )

    fun insertEventId(eventId: String) = settings.putString("EVENT_ID", eventId)

    fun deleteEventId() = settings.remove("EVENT_ID")

    fun getEventId(): String =
        settings.getStringOrNull("EVENT_ID") ?: throw EventSavedException()

    fun fetchEventId(): Flow<String> =
        settings.getStringOrNullFlow("EVENT_ID").map { it ?: throw EventSavedException() }

    fun fetchEvent(eventId: String): Flow<EventUi> = db.transactionWithResult {
        return@transactionWithResult db.eventQueries.selectEvent(eventId, eventMapper).asFlow()
            .combineTransform(
                db.ticketQueries.selectTicket(eventId, ticketMapper).asFlow()
            ) { event, ticket ->
                val eventInfo = event.executeAsOneOrNull() ?: return@combineTransform
                emit(EventUi(eventInfo = eventInfo, ticket = ticket.executeAsOneOrNull()))
            }
    }

    fun fetchCurrentEvent(): Flow<EventInfoUi?> {
        val eventId = settings.getStringOrNull("EVENT_ID") ?: return flow { emit(null) }
        return db.eventQueries.selectEvent(eventId, eventMapper)
            .asFlow()
            .mapToOneOrNull(dispatcher)
    }

    fun fetchQAndA(eventId: String): Flow<ImmutableList<QuestionAndResponseUi>> = db.transactionWithResult {
        return@transactionWithResult combine(
            db.qAndAQueries.selectQAndA(eventId).asFlow().mapToList(dispatcher),
            db.qAndAQueries.selectQAndAActions(eventId).asFlow().mapToList(dispatcher),
            transform = { qAndADb, actionsDb ->
                qAndADb.map { qanda ->
                    QuestionAndResponseUi(
                        question = qanda.question,
                        response = qanda.response,
                        actions = actionsDb
                            .filter { it.qanda_id == qanda.order_ }
                            .sortedBy { it.order_ }
                            .map { QuestionAndResponseActionUi(label = it.label, url = it.url) }
                            .toImmutableList()
                    )
                }.toImmutableList()
            }
        )
    }

    fun fetchMenus(eventId: String): Flow<ImmutableList<MenuItemUi>> =
        db.menuQueries.selectMenus(eventId, menuMapper)
            .asFlow()
            .mapToList(dispatcher)
            .map { it.toImmutableList() }

    fun fetchCoC(eventId: String): Flow<CoCUi> =
        db.eventQueries.selectCoc(eventId, cocMapper).asFlow().mapToOne(dispatcher)

    fun insertEvent(event: EventV3, qAndA: List<QuestionAndResponse>) = db.transaction {
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
            openfeedback_project_id = eventDb.openfeedback_project_id,
            contact_email = eventDb.contact_email,
            contact_phone = eventDb.contact_phone,
            twitter = eventDb.twitter,
            twitter_url = eventDb.twitter_url,
            linkedin = eventDb.linkedin,
            linkedin_url = eventDb.linkedin_url,
            faq_url = eventDb.faq_url,
            coc_url = eventDb.coc_url,
            updated_at = eventDb.updated_at
        )
        qAndA.forEach { qAndA ->
            db.qAndAQueries.insertQAndA(
                qAndA.order.toLong(),
                eventDb.id,
                qAndA.question,
                qAndA.response
            )
            qAndA.actions.forEach {
                db.qAndAQueries.insertQAndAAction(
                    id = "${qAndA.id}-${it.order}",
                    order_ = it.order.toLong(),
                    event_id = eventDb.id,
                    qanda_id = qAndA.order.toLong(),
                    label = it.label,
                    url = it.url
                )
            }
        }
        event.menus.forEach {
            db.menuQueries.insertMenu(it.name, it.dish, it.accompaniment, it.dessert, event.id)
        }
        db.featuresActivatedQueries.insertFeatures(
            event_id = eventDb.id,
            has_networking = event.features.hasNetworking,
            has_speaker_list = event.features.hasSpeakerList,
            has_partner_list = event.features.hasPartnerList,
            has_menus = event.features.hasMenus,
            has_qanda = event.features.hasQAndA,
            has_billet_web_ticket = event.features.hasBilletWebTicket
        )
    }

    fun insertEventItems(future: List<EventItemList>, past: List<EventItemList>) = db.transaction {
        future.forEach {
            val itemDb = it.convertToModelDb(false)
            db.eventQueries.insertEventItem(
                id = itemDb.id,
                name = itemDb.name,
                date = itemDb.date,
                timestamp = itemDb.timestamp,
                past = itemDb.past
            )
        }
        past.forEach {
            val itemDb = it.convertToModelDb(true)
            db.eventQueries.insertEventItem(
                id = itemDb.id,
                name = itemDb.name,
                date = itemDb.date,
                timestamp = itemDb.timestamp,
                past = itemDb.past
            )
        }
    }

    fun updateTicket(eventId: String, qrCode: Image, barcode: String, attendee: Attendee?) =
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
