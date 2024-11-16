package com.paligot.confily.core.events

import com.paligot.confily.core.events.entities.CodeOfConduct
import com.paligot.confily.core.events.entities.Event
import com.paligot.confily.core.events.entities.EventItemList
import com.paligot.confily.core.events.entities.MenuItem
import com.paligot.confily.core.events.entities.QAndAAction
import com.paligot.confily.core.events.entities.QAndAItem
import com.paligot.confily.core.events.entities.Ticket
import com.paligot.confily.models.Attendee
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import com.paligot.confily.models.EventItemList as EventItemListNetworking

class EventDaoSettings(
    private val eventQueries: EventQueries,
    private val qAndAQueries: QAndAQueries,
    private val menuQueries: MenuQueries
) : EventDao {
    override fun fetchEventList(): Flow<EventItemList> = combine(
        flow = eventQueries.selectEventItem(past = true),
        flow2 = eventQueries.selectEventItem(past = false),
        transform = { past, future ->
            EventItemList(
                past = past.map(EventItemDb::convertToEntity),
                future = future.map(EventItemDb::convertToEntity)
            )
        }
    )

    override fun fetchEvent(eventId: String): Flow<Event?> =
        eventQueries.selectEvent(eventId).map(EventDb::convertToEntity)

    override fun fetchTicket(eventId: String): Flow<Ticket?> {
        return flowOf(null)
    }

    override fun fetchQAndA(eventId: String): Flow<ImmutableList<QAndAItem>> = combine(
        flow = qAndAQueries.selectQAndA(eventId),
        flow2 = qAndAQueries.selectQAndAActions(eventId),
        transform = { qAndADb, actionsDb ->
            qAndADb.map { qanda ->
                QAndAItem(
                    question = qanda.question,
                    answer = qanda.response,
                    actions = actionsDb
                        .filter { it.qandaId == qanda.order }
                        .sortedBy { it.order }
                        .map { QAndAAction(label = it.label, url = it.url) }
                )
            }.toImmutableList()
        }
    )

    override fun fetchMenus(eventId: String): Flow<ImmutableList<MenuItem>> =
        menuQueries.selectMenus(eventId)
            .map { menus -> menus.map { it.convertToEntity() }.toImmutableList() }

    override fun fetchCoC(eventId: String): Flow<CodeOfConduct> =
        eventQueries.selectCoc(eventId).map { it.convertToEntity() }

    override fun insertEventItems(
        future: List<EventItemListNetworking>,
        past: List<EventItemListNetworking>
    ) {
        future.forEach {
            eventQueries.insertEventItem(it.convertToModelDb(false))
        }
        past.forEach {
            eventQueries.insertEventItem(it.convertToModelDb(true))
        }
    }

    override fun updateTicket(
        eventId: String,
        qrCode: ByteArray,
        barcode: String,
        attendee: Attendee?
    ) {
        /* no-op */
    }
}
