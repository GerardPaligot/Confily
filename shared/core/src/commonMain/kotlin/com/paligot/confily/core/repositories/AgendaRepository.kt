package com.paligot.confily.core.repositories

import com.paligot.confily.core.api.ConferenceApi
import com.paligot.confily.core.api.exceptions.AgendaNotModifiedException
import com.paligot.confily.core.database.EventDao
import com.paligot.confily.core.database.FeaturesActivatedDao
import com.paligot.confily.core.database.PartnerDao
import com.paligot.confily.core.database.ScheduleDao
import com.paligot.confily.core.database.TalkDao
import com.paligot.confily.core.speakers.SpeakerDao
import com.paligot.confily.models.ui.CategoryUi
import com.paligot.confily.models.ui.CoCUi
import com.paligot.confily.models.ui.EventSessionItemUi
import com.paligot.confily.models.ui.EventUi
import com.paligot.confily.models.ui.FiltersUi
import com.paligot.confily.models.ui.FormatUi
import com.paligot.confily.models.ui.MenuItemUi
import com.paligot.confily.models.ui.PartnerGroupsUi
import com.paligot.confily.models.ui.PartnerItemUi
import com.paligot.confily.models.ui.QuestionAndResponseUi
import com.paligot.confily.models.ui.ScaffoldConfigUi
import com.paligot.confily.models.ui.TalkItemUi
import com.paligot.confily.models.ui.TalkUi
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

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
    fun partners(): Flow<PartnerGroupsUi>

    @NativeCoroutines
    fun partner(id: String): Flow<PartnerItemUi>

    @NativeCoroutines
    fun qanda(): Flow<ImmutableList<QuestionAndResponseUi>>

    @NativeCoroutines
    fun menus(): Flow<ImmutableList<MenuItemUi>>

    @NativeCoroutines
    fun coc(): Flow<CoCUi>

    @NativeCoroutines
    fun agenda(): Flow<ImmutableMap<String, com.paligot.confily.models.ui.AgendaUi>>

    @NativeCoroutines
    fun fetchNextTalks(date: String): Flow<ImmutableList<TalkItemUi>>

    @NativeCoroutines
    fun filters(): Flow<FiltersUi>

    @NativeCoroutines
    fun scheduleItem(scheduleId: String): Flow<TalkUi>

    @NativeCoroutines
    fun scheduleEventSessionItem(scheduleId: String): Flow<EventSessionItemUi>

    @NativeCoroutines
    fun hasFilterApplied(): Flow<Boolean>
    fun applyFavoriteFilter(selected: Boolean)
    fun applyCategoryFilter(categoryUi: CategoryUi, selected: Boolean)
    fun applyFormatFilter(formatUi: FormatUi, selected: Boolean)

    fun markAsRead(sessionId: String, isFavorite: Boolean)

    @FlowPreview
    @ExperimentalSettingsApi
    @ExperimentalCoroutinesApi
    object Factory {
        fun create(
            api: ConferenceApi,
            scheduleDao: ScheduleDao,
            speakerDao: SpeakerDao,
            talkDao: TalkDao,
            eventDao: EventDao,
            partnerDao: PartnerDao,
            featuresDao: FeaturesActivatedDao,
            qrCodeGenerator: QrCodeGenerator
        ): AgendaRepository = AgendaRepositoryImpl(
            api,
            scheduleDao,
            speakerDao,
            talkDao,
            eventDao,
            partnerDao,
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
    private val scheduleDao: ScheduleDao,
    private val speakerDao: SpeakerDao,
    private val talkDao: TalkDao,
    private val eventDao: EventDao,
    private val partnerDao: PartnerDao,
    private val featuresDao: FeaturesActivatedDao,
    private val qrCodeGenerator: QrCodeGenerator
) : AgendaRepository {
    override suspend fun fetchAndStoreAgenda() {
        val eventId = eventDao.getEventId()
        val etag = scheduleDao.lastEtag(eventId)
        try {
            val (newEtag, agenda) = api.fetchAgenda(eventId, etag)
            scheduleDao.saveAgenda(eventId, agenda)
            scheduleDao.updateEtag(eventId, newEtag)
        } catch (ex: AgendaNotModifiedException) {
            ex.printStackTrace()
        }
        val event = api.fetchEvent(eventId)
        val qanda = api.fetchQAndA(eventId)
        val partners = api.fetchPartners(eventId)
        eventDao.insertEvent(event, qanda)
        partnerDao.insertPartners(eventId, partners)
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

    override fun partners(): Flow<PartnerGroupsUi> = eventDao.fetchEventId()
        .flatMapConcat { partnerDao.fetchPartners(eventId = it) }

    override fun partner(id: String): Flow<PartnerItemUi> = eventDao.fetchEventId()
        .flatMapConcat { partnerDao.fetchPartner(eventId = it, id = id) }

    override fun qanda(): Flow<ImmutableList<QuestionAndResponseUi>> = eventDao.fetchEventId()
        .flatMapConcat { eventDao.fetchQAndA(eventId = it) }

    override fun menus(): Flow<ImmutableList<MenuItemUi>> = eventDao.fetchEventId()
        .flatMapConcat { eventDao.fetchMenus(eventId = it) }

    override fun coc(): Flow<CoCUi> = eventDao.fetchEventId()
        .flatMapConcat { eventDao.fetchCoC(eventId = it) }

    override fun agenda(): Flow<ImmutableMap<String, com.paligot.confily.models.ui.AgendaUi>> = eventDao.fetchEventId()
        .flatMapConcat { scheduleDao.fetchSchedules(eventId = it) }

    override fun fetchNextTalks(date: String): Flow<ImmutableList<TalkItemUi>> =
        eventDao.fetchEventId()
            .flatMapConcat { scheduleDao.fetchNextTalks(eventId = it, date = date) }

    override fun filters(): Flow<FiltersUi> = eventDao.fetchEventId()
        .flatMapConcat { scheduleDao.fetchFilters(it) }

    override fun hasFilterApplied(): Flow<Boolean> = eventDao.fetchEventId()
        .flatMapConcat { scheduleDao.fetchFiltersAppliedCount(eventId = it) }
        .map { it > 0 }

    override fun applyFavoriteFilter(selected: Boolean) = scheduleDao.applyFavoriteFilter(selected)

    override fun applyCategoryFilter(categoryUi: CategoryUi, selected: Boolean) =
        scheduleDao.applyCategoryFilter(categoryUi, eventDao.getEventId(), selected)

    override fun applyFormatFilter(formatUi: FormatUi, selected: Boolean) =
        scheduleDao.applyFormatFilter(formatUi, eventDao.getEventId(), selected)

    override fun markAsRead(sessionId: String, isFavorite: Boolean) = scheduleDao.markAsFavorite(
        eventId = eventDao.getEventId(),
        sessionId = sessionId,
        isFavorite = isFavorite
    )

    override fun scheduleItem(scheduleId: String): Flow<TalkUi> = eventDao.fetchEventId()
        .flatMapConcat { talkDao.fetchTalk(eventId = it, talkId = scheduleId) }

    override fun scheduleEventSessionItem(scheduleId: String): Flow<EventSessionItemUi> =
        eventDao.fetchEventId()
            .flatMapConcat { talkDao.fetchEventSession(eventId = it, sessionId = scheduleId) }
}
