package com.paligot.confily.core.events

import com.paligot.confily.core.events.entities.CodeOfConduct
import com.paligot.confily.core.events.entities.EventInfo
import com.paligot.confily.core.events.entities.EventItemList
import com.paligot.confily.core.events.entities.FeatureFlags
import com.paligot.confily.core.events.entities.MenuItem
import com.paligot.confily.core.events.entities.QAndAItem
import com.paligot.confily.core.events.entities.TeamMemberInfo
import com.paligot.confily.core.events.entities.TeamMemberItem
import com.paligot.confily.models.Attendee
import com.paligot.confily.models.EventV5
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import com.paligot.confily.models.EventItemList as EventItemListNetworking

interface EventDao {
    fun fetchEventList(): Flow<EventItemList>
    fun fetchEvent(eventId: String): Flow<EventInfo?>
    fun fetchQAndA(eventId: String, language: String): Flow<ImmutableList<QAndAItem>>
    fun fetchMenus(eventId: String): Flow<ImmutableList<MenuItem>>
    fun fetchCoC(eventId: String): Flow<CodeOfConduct>
    fun fetchTeamMembers(eventId: String): Flow<Map<String, List<TeamMemberItem>>>
    fun fetchTeamMember(eventId: String, memberId: String): Flow<TeamMemberInfo?>
    fun fetchFeatureFlags(eventId: String): Flow<FeatureFlags>
    fun insertEvent(event: EventV5)
    fun insertEventItems(future: List<EventItemListNetworking>, past: List<EventItemListNetworking>)
    fun updateTicket(eventId: String, qrCode: ByteArray, barcode: String, attendee: Attendee?)
}
