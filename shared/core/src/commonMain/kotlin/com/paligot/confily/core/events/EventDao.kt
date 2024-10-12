package com.paligot.confily.core.events

import com.paligot.confily.models.Attendee
import com.paligot.confily.models.EventItemList
import com.paligot.confily.models.ui.CoCUi
import com.paligot.confily.models.ui.EventInfoUi
import com.paligot.confily.models.ui.EventItemListUi
import com.paligot.confily.models.ui.EventUi
import com.paligot.confily.models.ui.MenuItemUi
import com.paligot.confily.models.ui.QuestionAndResponseUi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface EventDao {
    fun fetchEventList(): Flow<EventItemListUi>
    fun fetchEvent(eventId: String): Flow<EventInfoUi?>
    fun fetchEventAndTicket(eventId: String): Flow<EventUi>
    fun fetchQAndA(eventId: String): Flow<ImmutableList<QuestionAndResponseUi>>
    fun fetchMenus(eventId: String): Flow<ImmutableList<MenuItemUi>>
    fun fetchCoC(eventId: String): Flow<CoCUi>
    fun insertEventItems(future: List<EventItemList>, past: List<EventItemList>)
    fun updateTicket(eventId: String, qrCode: ByteArray, barcode: String, attendee: Attendee?)
}
