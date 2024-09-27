package com.paligot.confily.core.events

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.paligot.confily.core.exceptions.EventSavedException
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.models.Attendee
import com.paligot.confily.models.EventItemList
import com.paligot.confily.models.ui.CoCUi
import com.paligot.confily.models.ui.EventInfoUi
import com.paligot.confily.models.ui.EventItemListUi
import com.paligot.confily.models.ui.EventUi
import com.paligot.confily.models.ui.MenuItemUi
import com.paligot.confily.models.ui.QuestionAndResponseActionUi
import com.paligot.confily.models.ui.QuestionAndResponseUi
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalSettingsApi::class)
class EventDao(
    private val db: ConfilyDatabase,
    private val settings: ObservableSettings,
    private val dispatcher: CoroutineContext
) {
    fun fetchEventList(): Flow<EventItemListUi> = combine(
        db.eventQueries.selectEventItem(true, eventItemMapper).asFlow().mapToList(dispatcher),
        db.eventQueries.selectEventItem(false, eventItemMapper).asFlow().mapToList(dispatcher),
        transform = { past, future ->
            EventItemListUi(future = future.toImmutableList(), past = past.toImmutableList())
        }
    )

    fun getEventId(): String =
        settings.getStringOrNull("EVENT_ID") ?: throw EventSavedException()

    fun fetchEventId(): Flow<String> =
        settings.getStringOrNullFlow("EVENT_ID").map { it ?: throw EventSavedException() }

    fun fetchEvent(eventId: String): Flow<EventInfoUi?> =
        db.eventQueries.selectEvent(eventId, eventMapper)
            .asFlow()
            .mapToOneOrNull(dispatcher)

    fun insertEventId(eventId: String) = settings.putString("EVENT_ID", eventId)

    fun deleteEventId() = settings.remove("EVENT_ID")

    fun fetchEventAndTicket(eventId: String): Flow<EventUi> = db.transactionWithResult {
        return@transactionWithResult db.eventQueries.selectEvent(eventId, eventMapper).asFlow()
            .combineTransform(
                db.ticketQueries.selectTicket(eventId, ticketMapper).asFlow()
            ) { event, ticket ->
                val eventInfo = event.executeAsOneOrNull() ?: return@combineTransform
                emit(EventUi(eventInfo = eventInfo, ticket = ticket.executeAsOneOrNull()))
            }
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

    fun updateTicket(eventId: String, qrCode: ByteArray, barcode: String, attendee: Attendee?) =
        db.ticketQueries.insertUser(
            id = attendee?.id,
            ext_id = attendee?.idExt,
            event_id = eventId,
            email = attendee?.email,
            firstname = attendee?.firstname,
            lastname = attendee?.name,
            barcode = barcode,
            qrcode = qrCode
        )
}
