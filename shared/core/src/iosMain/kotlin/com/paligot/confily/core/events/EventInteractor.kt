package com.paligot.confily.core.events

import com.paligot.confily.core.events.entities.mapToEventInfoUi
import com.paligot.confily.core.events.entities.mapToEventItemListUi
import com.paligot.confily.core.events.entities.mapToMenuItemUi
import com.paligot.confily.core.networking.entities.mapToTicketUi
import com.paligot.confily.models.ui.EventItemListUi
import com.paligot.confily.models.ui.EventUi
import com.paligot.confily.models.ui.MenuItemUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class EventInteractor(
    private val repository: EventRepository
) {
    @NativeCoroutines
    suspend fun fetchAndStoreEventList() = repository.fetchAndStoreEventList()

    @NativeCoroutines
    suspend fun fetchAndStoreAgenda() = repository.fetchAndStoreAgenda()

    @NativeCoroutines
    fun events(): Flow<EventItemListUi> = repository.events()
        .map { eventItemList -> eventItemList.mapToEventItemListUi() }

    @NativeCoroutines
    fun event(): Flow<EventUi?> = combine(
        flow = repository.event(),
        flow2 = repository.ticket(),
        transform = { event, ticket ->
            if (event == null) return@combine null
            EventUi(
                eventInfo = event.mapToEventInfoUi(),
                ticket = ticket?.mapToTicketUi()
            )
        }
    )

    @NativeCoroutines
    fun menus(): Flow<ImmutableList<MenuItemUi>> = repository.menus()
        .map { menus -> menus.map { it.mapToMenuItemUi() }.toImmutableList() }

    @NativeCoroutines
    suspend fun insertOrUpdateTicket(barcode: String) = repository.insertOrUpdateTicket(barcode)
}
