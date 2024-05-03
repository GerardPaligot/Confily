package org.gdglille.devfest.repositories

import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.database.FeaturesActivatedDao
import org.gdglille.devfest.database.PartnerDao
import org.gdglille.devfest.database.ScheduleDao
import org.gdglille.devfest.database.SpeakerDao
import org.gdglille.devfest.database.TalkDao
import org.gdglille.devfest.exceptions.AgendaNotModifiedException
import org.gdglille.devfest.models.ui.AgendaUi
import org.gdglille.devfest.models.ui.CategoryUi
import org.gdglille.devfest.models.ui.CoCUi
import org.gdglille.devfest.models.ui.EventUi
import org.gdglille.devfest.models.ui.FiltersUi
import org.gdglille.devfest.models.ui.FormatUi
import org.gdglille.devfest.models.ui.MenuItemUi
import org.gdglille.devfest.models.ui.PartnerGroupsUi
import org.gdglille.devfest.models.ui.PartnerItemUi
import org.gdglille.devfest.models.ui.QuestionAndResponseUi
import org.gdglille.devfest.models.ui.ScaffoldConfigUi
import org.gdglille.devfest.models.ui.SpeakerUi
import org.gdglille.devfest.models.ui.TalkItemUi
import org.gdglille.devfest.models.ui.TalkUi
import org.gdglille.devfest.network.ConferenceApi

interface AgendaRepository {
    suspend fun fetchAndStoreAgenda()
    suspend fun insertOrUpdateTicket(barcode: String)
    fun scaffoldConfig(): Flow<ScaffoldConfigUi>
    fun event(): Flow<EventUi>
    fun partners(): Flow<PartnerGroupsUi>
    fun partner(id: String): Flow<PartnerItemUi>
    fun qanda(): Flow<ImmutableList<QuestionAndResponseUi>>
    fun menus(): Flow<ImmutableList<MenuItemUi>>
    fun coc(): Flow<CoCUi>
    fun agenda(): Flow<ImmutableMap<String, AgendaUi>>
    fun fetchNextTalks(date: String): Flow<ImmutableList<TalkItemUi>>
    fun filters(): Flow<FiltersUi>
    fun scheduleItem(scheduleId: String): Flow<TalkUi>
    fun hasFilterApplied(): Flow<Boolean>
    fun applyFavoriteFilter(selected: Boolean)
    fun applyCategoryFilter(categoryUi: CategoryUi, selected: Boolean)
    fun applyFormatFilter(formatUi: FormatUi, selected: Boolean)
    fun speaker(speakerId: String): Flow<SpeakerUi>
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
    @NativeCoroutineScope
    private val coroutineScope: CoroutineScope = MainScope()

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

    override fun agenda(): Flow<ImmutableMap<String, AgendaUi>> = eventDao.fetchEventId()
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

    override fun speaker(speakerId: String): Flow<SpeakerUi> = eventDao.fetchEventId()
        .flatMapConcat { speakerDao.fetchSpeaker(eventId = it, speakerId = speakerId) }

    override fun markAsRead(sessionId: String, isFavorite: Boolean) = scheduleDao.markAsFavorite(
        eventId = eventDao.getEventId(),
        sessionId = sessionId,
        isFavorite = isFavorite
    )

    override fun scheduleItem(scheduleId: String): Flow<TalkUi> = eventDao.fetchEventId()
        .flatMapConcat { talkDao.fetchTalk(eventId = it, talkId = scheduleId) }
}
