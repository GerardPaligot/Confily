package com.paligot.confily.core.events

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.paligot.confily.core.events.entities.CodeOfConduct
import com.paligot.confily.core.events.entities.Event
import com.paligot.confily.core.events.entities.EventItemList
import com.paligot.confily.core.events.entities.MenuItem
import com.paligot.confily.core.events.entities.QAndAAction
import com.paligot.confily.core.events.entities.QAndAItem
import com.paligot.confily.core.events.entities.Ticket
import com.paligot.confily.db.ConfilyDatabase
import com.paligot.confily.models.Attendee
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext
import com.paligot.confily.models.EventItemList as EventItemListNetworking

class EventDaoSQLDelight(
    private val db: ConfilyDatabase,
    private val dispatcher: CoroutineContext
) : EventDao {
    override fun fetchEventList(): Flow<EventItemList> = combine(
        db.eventQueries.selectEventItem(true, eventItemMapper).asFlow().mapToList(dispatcher),
        db.eventQueries.selectEventItem(false, eventItemMapper).asFlow().mapToList(dispatcher),
        transform = { past, future -> EventItemList(future = future, past = past) }
    )

    override fun fetchEvent(eventId: String): Flow<Event?> =
        db.eventQueries.selectEvent(eventId, eventMapper)
            .asFlow()
            .mapToOneOrNull(dispatcher)

    override fun fetchTicket(eventId: String): Flow<Ticket?> =
        db.ticketQueries.selectTicket(eventId, ticketMapper)
            .asFlow()
            .mapToOneOrNull(dispatcher)

    override fun fetchQAndA(eventId: String): Flow<ImmutableList<QAndAItem>> =
        db.transactionWithResult {
            return@transactionWithResult combine(
                db.qAndAQueries.selectQAndA(eventId).asFlow().mapToList(dispatcher),
                db.qAndAQueries.selectQAndAActions(eventId).asFlow().mapToList(dispatcher),
                transform = { qAndADb, actionsDb ->
                    qAndADb.map { qanda ->
                        QAndAItem(
                            question = qanda.question,
                            answer = qanda.response,
                            actions = actionsDb
                                .filter { it.qanda_id == qanda.order_ }
                                .sortedBy { it.order_ }
                                .map { QAndAAction(label = it.label, url = it.url) }
                        )
                    }.toImmutableList()
                }
            )
        }

    override fun fetchMenus(eventId: String): Flow<ImmutableList<MenuItem>> =
        db.menuQueries.selectMenus(eventId, menuMapper)
            .asFlow()
            .mapToList(dispatcher)
            .map { it.toImmutableList() }

    override fun fetchCoC(eventId: String): Flow<CodeOfConduct> =
        db.eventQueries.selectCoc(eventId, cocMapper).asFlow().mapToOne(dispatcher)

    override fun insertEventItems(
        future: List<EventItemListNetworking>,
        past: List<EventItemListNetworking>
    ) = db.transaction {
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

    override fun updateTicket(
        eventId: String,
        qrCode: ByteArray,
        barcode: String,
        attendee: Attendee?
    ) = db.ticketQueries.insertUser(
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
