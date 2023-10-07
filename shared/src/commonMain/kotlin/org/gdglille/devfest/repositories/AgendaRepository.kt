package org.gdglille.devfest.repositories

import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.database.FeaturesActivatedDao
import org.gdglille.devfest.database.PartnerDao
import org.gdglille.devfest.database.ScheduleDao
import org.gdglille.devfest.database.SpeakerDao
import org.gdglille.devfest.database.TalkDao
import org.gdglille.devfest.exceptions.AgendaNotModifiedException
import org.gdglille.devfest.models.AgendaUi
import org.gdglille.devfest.models.CategoryUi
import org.gdglille.devfest.models.CoCUi
import org.gdglille.devfest.models.EventUi
import org.gdglille.devfest.models.FiltersUi
import org.gdglille.devfest.models.FormatUi
import org.gdglille.devfest.models.MenuItemUi
import org.gdglille.devfest.models.PartnerGroupsUi
import org.gdglille.devfest.models.PartnerItemUi
import org.gdglille.devfest.models.QuestionAndResponseUi
import org.gdglille.devfest.models.ScaffoldConfigUi
import org.gdglille.devfest.models.SpeakerUi
import org.gdglille.devfest.models.TalkUi
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
    fun agenda(date: String): Flow<AgendaUi>
    fun filters(): Flow<FiltersUi>
    fun hasFilterApplied(): Flow<Boolean>
    fun applyFavoriteFilter(selected: Boolean)
    fun applyCategoryFilter(categoryUi: CategoryUi, selected: Boolean)
    fun applyFormatFilter(formatUi: FormatUi, selected: Boolean)
    fun speaker(speakerId: String): Flow<SpeakerUi>
    fun markAsRead(sessionId: String, isFavorite: Boolean)
    fun scheduleItem(scheduleId: String): TalkUi

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
        val eventId = eventDao.fetchEventId()
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
        val eventId = eventDao.fetchEventId()
        val attendee = try {
            val attendee = api.fetchAttendee(eventId, barcode)
            attendee
        } catch (ignored: Throwable) {
            null
        }
        val qrCode = qrCodeGenerator.generate(barcode)
        eventDao.updateTicket(eventId, qrCode, barcode, attendee)
    }

    override fun scaffoldConfig(): Flow<ScaffoldConfigUi> = featuresDao.fetchFeatures(
        eventId = eventDao.fetchEventId()
    )

    override fun event(): Flow<EventUi> = eventDao.fetchEvent(
        eventId = eventDao.fetchEventId()
    )

    override fun partners(): Flow<PartnerGroupsUi> = partnerDao.fetchPartners(
        eventId = eventDao.fetchEventId()
    )

    override fun partner(id: String): Flow<PartnerItemUi> = partnerDao.fetchPartner(
        eventId = eventDao.fetchEventId(),
        id = id
    )

    override fun qanda(): Flow<ImmutableList<QuestionAndResponseUi>> = eventDao.fetchQAndA(
        eventId = eventDao.fetchEventId()
    )

    override fun menus(): Flow<ImmutableList<MenuItemUi>> = eventDao.fetchMenus(
        eventId = eventDao.fetchEventId()
    )

    override fun coc(): Flow<CoCUi> = eventDao.fetchCoC(
        eventId = eventDao.fetchEventId()
    )

    override fun agenda(date: String): Flow<AgendaUi> = scheduleDao.fetchSchedules(
        eventId = eventDao.fetchEventId(),
        date = date
    )

    override fun filters(): Flow<FiltersUi> = scheduleDao.fetchFilters(
        eventId = eventDao.fetchEventId()
    )

    override fun hasFilterApplied(): Flow<Boolean> = scheduleDao.fetchFiltersAppliedCount(
        eventId = eventDao.fetchEventId()
    ).map { it > 0 }

    override fun applyFavoriteFilter(selected: Boolean) = scheduleDao.applyFavoriteFilter(selected)

    override fun applyCategoryFilter(categoryUi: CategoryUi, selected: Boolean) =
        scheduleDao.applyCategoryFilter(categoryUi, eventDao.fetchEventId(), selected)

    override fun applyFormatFilter(formatUi: FormatUi, selected: Boolean) =
        scheduleDao.applyFormatFilter(formatUi, eventDao.fetchEventId(), selected)

    override fun speaker(speakerId: String): Flow<SpeakerUi> = speakerDao.fetchSpeaker(
        eventId = eventDao.fetchEventId(),
        speakerId = speakerId
    )

    override fun markAsRead(sessionId: String, isFavorite: Boolean) = scheduleDao.markAsFavorite(
        eventId = eventDao.fetchEventId(),
        sessionId = sessionId,
        isFavorite = isFavorite
    )

    override fun scheduleItem(scheduleId: String): TalkUi = talkDao.fetchTalk(
        eventId = eventDao.fetchEventId(),
        talkId = scheduleId
    )
}
