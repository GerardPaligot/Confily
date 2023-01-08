package org.gdglille.devfest.repositories

import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.models.EventItemListUi
import org.gdglille.devfest.network.ConferenceApi

interface EventRepository {
    suspend fun fetchAndStoreEventList()
    fun events(): Flow<EventItemListUi>

    object Factory {
        fun create(api: ConferenceApi, eventDao: EventDao): EventRepository =
            EventRepositoryImpl(api = api, eventDao = eventDao)
    }
}

class EventRepositoryImpl(
    val api: ConferenceApi,
    val eventDao: EventDao
) : EventRepository {
    @NativeCoroutineScope
    private val coroutineScope: CoroutineScope = MainScope()

    override suspend fun fetchAndStoreEventList() = coroutineScope {
        eventDao.insertEventItems(api.fetchEventList().future)
    }

    override fun events(): Flow<EventItemListUi> = eventDao.fetchFutureEvent()
}
