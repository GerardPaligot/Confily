package com.paligot.confily.core.repositories

import com.paligot.confily.core.api.ConferenceApi
import com.paligot.confily.core.api.exceptions.AgendaNotModifiedException
import com.paligot.confily.core.database.AgendaDao
import com.paligot.confily.core.database.EventDao
import com.paligot.confily.core.database.FeaturesActivatedDao
import com.paligot.confily.models.ui.CoCUi
import com.paligot.confily.models.ui.EventUi
import com.paligot.confily.models.ui.MenuItemUi
import com.paligot.confily.models.ui.QuestionAndResponseUi
import com.paligot.confily.models.ui.ScaffoldConfigUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat

interface AgendaRepository {
    @NativeCoroutines
    suspend fun fetchAndStoreAgenda()

    @NativeCoroutines
    suspend fun insertOrUpdateTicket(barcode: String)

    @NativeCoroutines
    fun scaffoldConfig(): Flow<ScaffoldConfigUi>

    @NativeCoroutines
    fun event(): Flow<EventUi>

    @NativeCoroutines
    fun qanda(): Flow<ImmutableList<QuestionAndResponseUi>>

    @NativeCoroutines
    fun menus(): Flow<ImmutableList<MenuItemUi>>

    @NativeCoroutines
    fun coc(): Flow<CoCUi>

    @FlowPreview
    @ExperimentalSettingsApi
    @ExperimentalCoroutinesApi
    object Factory {
        fun create(
            api: ConferenceApi,
            agendaDao: AgendaDao,
            eventDao: EventDao,
            featuresDao: FeaturesActivatedDao,
            qrCodeGenerator: QrCodeGenerator
        ): AgendaRepository = AgendaRepositoryImpl(
            api,
            agendaDao,
            eventDao,
            featuresDao,
            qrCodeGenerator
        )
    }
}

@FlowPreview
@ExperimentalSettingsApi
@ExperimentalCoroutinesApi
class AgendaRepositoryImpl(
    private val api: ConferenceApi,
    private val agendaDao: AgendaDao,
    private val eventDao: EventDao,
    private val featuresDao: FeaturesActivatedDao,
    private val qrCodeGenerator: QrCodeGenerator
) : AgendaRepository {
    override suspend fun fetchAndStoreAgenda() {
        val eventId = eventDao.getEventId()
        val etag = agendaDao.lastEtag(eventId)
        try {
            val (newEtag, agenda) = api.fetchAgenda(eventId, etag)
            agendaDao.saveAgenda(eventId, agenda)
            agendaDao.updateEtag(eventId, newEtag)
        } catch (ex: AgendaNotModifiedException) {
            ex.printStackTrace()
        }
        val event = api.fetchEvent(eventId)
        val qanda = api.fetchQAndA(eventId)
        val partners = api.fetchPartners(eventId)
        eventDao.insertEvent(event, qanda)
        agendaDao.insertPartners(eventId, partners)
    }

    override suspend fun insertOrUpdateTicket(barcode: String) {
        val eventId = eventDao.getEventId()
        val attendee = try {
            val attendee = api.fetchAttendee(eventId, barcode)
            attendee
        } catch (ignored: Throwable) {
            null
        }
        val qrCode = qrCodeGenerator.generate(barcode)
        eventDao.updateTicket(eventId, qrCode, barcode, attendee)
    }

    override fun scaffoldConfig(): Flow<ScaffoldConfigUi> = featuresDao.fetchFeatures()

    override fun event(): Flow<EventUi> = eventDao.fetchEventId()
        .flatMapConcat { eventDao.fetchEvent(eventId = it) }

    override fun qanda(): Flow<ImmutableList<QuestionAndResponseUi>> = eventDao.fetchEventId()
        .flatMapConcat { eventDao.fetchQAndA(eventId = it) }

    override fun menus(): Flow<ImmutableList<MenuItemUi>> = eventDao.fetchEventId()
        .flatMapConcat { eventDao.fetchMenus(eventId = it) }

    override fun coc(): Flow<CoCUi> = eventDao.fetchEventId()
        .flatMapConcat { eventDao.fetchCoC(eventId = it) }
}
