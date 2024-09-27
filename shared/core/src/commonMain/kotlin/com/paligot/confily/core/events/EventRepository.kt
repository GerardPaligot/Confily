package com.paligot.confily.core.events

import com.paligot.confily.core.QrCodeGenerator
import com.paligot.confily.core.api.ConferenceApi
import com.paligot.confily.core.db.ConferenceSettings
import com.paligot.confily.core.db.EventSavedException
import com.paligot.confily.models.ui.CoCUi
import com.paligot.confily.models.ui.EventInfoUi
import com.paligot.confily.models.ui.EventItemListUi
import com.paligot.confily.models.ui.EventUi
import com.paligot.confily.models.ui.MenuItemUi
import com.paligot.confily.models.ui.QuestionAndResponseUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat

interface EventRepository {
    @NativeCoroutines
    suspend fun fetchAndStoreEventList()

    @NativeCoroutines
    fun events(): Flow<EventItemListUi>

    @NativeCoroutines
    fun event(): Flow<EventInfoUi?>

    @NativeCoroutines
    fun eventAndTicket(): Flow<EventUi>

    @NativeCoroutines
    fun qanda(): Flow<ImmutableList<QuestionAndResponseUi>>

    @NativeCoroutines
    fun menus(): Flow<ImmutableList<MenuItemUi>>

    @NativeCoroutines
    fun coc(): Flow<CoCUi>

    fun isInitialized(defaultEvent: String? = null): Boolean
    fun saveEventId(eventId: String)
    fun deleteEventId()

    @NativeCoroutines
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

@OptIn(ExperimentalCoroutinesApi::class)
class EventRepositoryImpl(
    private val api: ConferenceApi,
    private val settings: ConferenceSettings,
    private val eventDao: EventDao,
    private val qrCodeGenerator: QrCodeGenerator
) : EventRepository {
    override suspend fun fetchAndStoreEventList() = coroutineScope {
        val events = api.fetchEventList()
        eventDao.insertEventItems(future = events.future, past = events.past)
    }

    override fun events(): Flow<EventItemListUi> = eventDao.fetchEventList()

    override fun event(): Flow<EventInfoUi?> = settings.fetchEventId()
        .flatMapConcat { eventDao.fetchEvent(eventId = it) }

    override fun eventAndTicket(): Flow<EventUi> = settings.fetchEventId()
        .flatMapConcat { eventDao.fetchEventAndTicket(eventId = it) }

    override fun qanda(): Flow<ImmutableList<QuestionAndResponseUi>> = settings.fetchEventId()
        .flatMapConcat { eventDao.fetchQAndA(eventId = it) }

    override fun menus(): Flow<ImmutableList<MenuItemUi>> = settings.fetchEventId()
        .flatMapConcat { eventDao.fetchMenus(eventId = it) }

    override fun coc(): Flow<CoCUi> = settings.fetchEventId()
        .flatMapConcat { eventDao.fetchCoC(eventId = it) }

    override fun isInitialized(defaultEvent: String?): Boolean = try {
        settings.getEventId()
        true
    } catch (_: EventSavedException) {
        if (defaultEvent != null) {
            settings.insertEventId(defaultEvent)
            true
        } else {
            false
        }
    }

    override fun saveEventId(eventId: String) {
        settings.insertEventId(eventId)
    }

    override fun deleteEventId() {
        settings.deleteEventId()
    }

    override suspend fun insertOrUpdateTicket(barcode: String) {
        val eventId = settings.getEventId()
        val attendee = try {
            val attendee = api.fetchAttendee(eventId, barcode)
            attendee
        } catch (ignored: Throwable) {
            null
        }
        val qrCode = qrCodeGenerator.generate(barcode)
        eventDao.updateTicket(eventId, qrCode, barcode, attendee)
    }
}
