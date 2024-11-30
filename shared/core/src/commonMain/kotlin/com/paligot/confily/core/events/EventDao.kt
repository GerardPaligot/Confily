package com.paligot.confily.core.events

import com.paligot.confily.core.events.entities.CodeOfConduct
import com.paligot.confily.core.events.entities.Event
import com.paligot.confily.core.events.entities.EventItemList
import com.paligot.confily.core.events.entities.MenuItem
import com.paligot.confily.core.events.entities.QAndAItem
import com.paligot.confily.models.Attendee
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import com.paligot.confily.models.EventItemList as EventItemListNetworking

interface EventDao {
    fun fetchEventList(): Flow<EventItemList>
    fun fetchEvent(eventId: String): Flow<Event?>
    fun fetchQAndA(eventId: String): Flow<ImmutableList<QAndAItem>>
    fun fetchMenus(eventId: String): Flow<ImmutableList<MenuItem>>
    fun fetchCoC(eventId: String): Flow<CodeOfConduct>
    fun insertEventItems(future: List<EventItemListNetworking>, past: List<EventItemListNetworking>)
    fun updateTicket(eventId: String, qrCode: ByteArray, barcode: String, attendee: Attendee?)
}
