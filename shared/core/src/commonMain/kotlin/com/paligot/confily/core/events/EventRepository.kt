package com.paligot.confily.core.events

import com.paligot.confily.core.QrCodeGenerator
import com.paligot.confily.core.api.ConferenceApi
import com.paligot.confily.core.events.entities.CodeOfConduct
import com.paligot.confily.core.events.entities.Event
import com.paligot.confily.core.events.entities.EventItemList
import com.paligot.confily.core.events.entities.FeatureFlags
import com.paligot.confily.core.events.entities.MenuItem
import com.paligot.confily.core.events.entities.QAndAItem
import com.paligot.confily.core.kvalue.ConferenceSettings
import com.paligot.confily.core.networking.UserDao
import com.paligot.confily.core.networking.entities.UserTicket
import com.paligot.confily.core.partners.PartnerDao
import com.paligot.confily.core.schedules.SessionDao
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun fetchAndStoreEventList()
    suspend fun fetchAndStoreAgenda()
    fun events(): Flow<EventItemList>
    fun event(): Flow<Event?>
    fun ticket(): Flow<UserTicket?>
    fun qanda(): Flow<ImmutableList<QAndAItem>>
    fun menus(): Flow<ImmutableList<MenuItem>>
    fun coc(): Flow<CodeOfConduct>
    fun featureFlags(): Flow<FeatureFlags>
    fun isInitialized(defaultEvent: String? = null): Boolean
    fun saveEventId(eventId: String)
    fun deleteEventId()
    suspend fun insertOrUpdateTicket(barcode: String)

    object Factory {
        fun create(
            api: ConferenceApi,
            settings: ConferenceSettings,
            eventDao: EventDao,
            sessionDao: SessionDao,
            userDao: UserDao,
            partnerDao: PartnerDao,
            qrCodeGenerator: QrCodeGenerator
        ): EventRepository = EventRepositoryImpl(
            api,
            settings,
            eventDao,
            sessionDao,
            userDao,
            partnerDao,
            qrCodeGenerator
        )
    }
}
