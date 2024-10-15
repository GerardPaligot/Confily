package com.paligot.confily.core.events

import com.paligot.confily.models.Attendee
import com.paligot.confily.models.EventItemList
import com.paligot.confily.models.ui.CoCUi
import com.paligot.confily.models.ui.EventInfoUi
import com.paligot.confily.models.ui.EventItemListUi
import com.paligot.confily.models.ui.EventUi
import com.paligot.confily.models.ui.MenuItemUi
import com.paligot.confily.models.ui.QuestionAndResponseActionUi
import com.paligot.confily.models.ui.QuestionAndResponseUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class EventDaoSettings(
    private val eventQueries: EventQueries,
    private val qAndAQueries: QAndAQueries,
    private val menuQueries: MenuQueries
) : EventDao {
    override fun fetchEventList(): Flow<EventItemListUi> {
        return combine(
            flow = eventQueries.selectEventItem(past = true),
            flow2 = eventQueries.selectEventItem(past = false),
            transform = { past, future ->
                EventItemListUi(
                    past = past.map { it.convertToUi() }.toImmutableList(),
                    future = future.map { it.convertToUi() }.toImmutableList()
                )
            }
        )
    }

    override fun fetchEvent(eventId: String): Flow<EventInfoUi?> =
        eventQueries.selectEvent(eventId)
            .map { it.convertToEventInfoUi() }

    override fun fetchEventAndTicket(eventId: String): Flow<EventUi> =
        eventQueries.selectEvent(eventId)
            .map { EventUi(eventInfo = it.convertToEventInfoUi(), ticket = null) }

    override fun fetchQAndA(eventId: String): Flow<ImmutableList<QuestionAndResponseUi>> = combine(
        flow = qAndAQueries.selectQAndA(eventId),
        flow2 = qAndAQueries.selectQAndAActions(eventId),
        transform = { qAndADb, actionsDb ->
            qAndADb.map { qanda ->
                QuestionAndResponseUi(
                    question = qanda.question,
                    response = qanda.response,
                    actions = actionsDb
                        .filter { it.qandaId == qanda.order }
                        .sortedBy { it.order }
                        .map { QuestionAndResponseActionUi(label = it.label, url = it.url) }
                        .toImmutableList()
                )
            }.toImmutableList()
        }
    )

    override fun fetchMenus(eventId: String): Flow<ImmutableList<MenuItemUi>> =
        menuQueries.selectMenus(eventId)
            .map { menus -> menus.map { it.convertToUi() }.toImmutableList() }

    override fun fetchCoC(eventId: String): Flow<CoCUi> =
        eventQueries.selectCoc(eventId).map { it.convertToUi() }

    override fun insertEventItems(future: List<EventItemList>, past: List<EventItemList>) {
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
