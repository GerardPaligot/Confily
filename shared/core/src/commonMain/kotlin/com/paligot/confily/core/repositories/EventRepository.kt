package com.paligot.confily.core.repositories

import com.paligot.confily.core.database.EventDao
import com.paligot.confily.core.exceptions.EventSavedException
import com.paligot.confily.core.network.ConferenceApi
import com.paligot.confily.models.ui.EventInfoUi
import com.paligot.confily.models.ui.EventItemListUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    @NativeCoroutines
    suspend fun fetchAndStoreEventList()

    @NativeCoroutines
    fun events(): Flow<EventItemListUi>

    @NativeCoroutines
    fun currentEvent(): Flow<EventInfoUi?>
    fun isInitialized(defaultEvent: String? = null): Boolean
    fun saveEventId(eventId: String)
    fun deleteEventId()

    object Factory {
        fun create(api: ConferenceApi, eventDao: EventDao): EventRepository =
            EventRepositoryImpl(api = api, eventDao = eventDao)
    }
}

class EventRepositoryImpl(
    val api: ConferenceApi,
    val eventDao: EventDao
) : EventRepository {
    override suspend fun fetchAndStoreEventList() = coroutineScope {
        val events = api.fetchEventList()
        eventDao.insertEventItems(future = events.future, past = events.past)
    }

    override fun events(): Flow<EventItemListUi> = eventDao.fetchEventList()
    override fun currentEvent(): Flow<EventInfoUi?> = eventDao.fetchCurrentEvent()

    override fun isInitialized(defaultEvent: String?): Boolean = try {
        eventDao.getEventId()
        true
    } catch (_: EventSavedException) {
        if (defaultEvent != null) {
            eventDao.insertEventId(defaultEvent)
            true
        } else {
            false
        }
    }

    override fun saveEventId(eventId: String) {
        eventDao.insertEventId(eventId)
    }

    override fun deleteEventId() {
        eventDao.deleteEventId()
    }
}
