package com.paligot.confily.core.events

import com.paligot.confily.core.QrCodeGenerator
import com.paligot.confily.core.api.ConferenceApi
import com.paligot.confily.core.events.entities.CodeOfConduct
import com.paligot.confily.core.events.entities.Event
import com.paligot.confily.core.events.entities.EventItemList
import com.paligot.confily.core.events.entities.MenuItem
import com.paligot.confily.core.events.entities.QAndAItem
import com.paligot.confily.core.events.entities.Ticket
import com.paligot.confily.core.kvalue.ConferenceSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun fetchAndStoreEventList()
    fun events(): Flow<EventItemList>
    fun event(): Flow<Event?>
    fun ticket(): Flow<Ticket?>
    fun qanda(): Flow<ImmutableList<QAndAItem>>
    fun menus(): Flow<ImmutableList<MenuItem>>
    fun coc(): Flow<CodeOfConduct>
    fun isInitialized(defaultEvent: String? = null): Boolean
    fun saveEventId(eventId: String)
    fun deleteEventId()
    suspend fun insertOrUpdateTicket(barcode: String)

    object Factory {
        fun create(
            api: ConferenceApi,
            settings: ConferenceSettings,
            eventDao: EventDao,
            qrCodeGenerator: QrCodeGenerator
        ): EventRepository = EventRepositoryImpl(api, settings, eventDao, qrCodeGenerator)
    }
}
