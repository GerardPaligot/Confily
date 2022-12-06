package org.gdglille.devfest.repositories

import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.database.FeaturesActivatedDao
import org.gdglille.devfest.database.PartnerDao
import org.gdglille.devfest.database.ScheduleDao
import org.gdglille.devfest.database.SpeakerDao
import org.gdglille.devfest.database.TalkDao
import org.gdglille.devfest.models.AgendaUi
import org.gdglille.devfest.models.CoCUi
import org.gdglille.devfest.models.EventUi
import org.gdglille.devfest.models.MenuItemUi
import org.gdglille.devfest.models.PartnerGroupsUi
import org.gdglille.devfest.models.QuestionAndResponseUi
import org.gdglille.devfest.models.ScaffoldConfigUi
import org.gdglille.devfest.models.SpeakerUi
import org.gdglille.devfest.models.TalkUi
import org.gdglille.devfest.network.ConferenceApi

interface AgendaRepository {
    suspend fun fetchAndStoreAgenda()
    fun isFavoriteToggled(): Flow<Boolean>
    fun toggleFavoriteFiltering()
    suspend fun insertOrUpdateTicket(barcode: String)
    fun scaffoldConfig(): Flow<ScaffoldConfigUi>
    fun event(): Flow<EventUi>
    fun partners(): Flow<PartnerGroupsUi>
    fun qanda(): Flow<List<QuestionAndResponseUi>>
    fun menus(): Flow<List<MenuItemUi>>
    fun coc(): Flow<CoCUi>
    fun agenda(date: String): Flow<AgendaUi>
    fun speaker(speakerId: String): Flow<SpeakerUi>
    fun markAsRead(scheduleId: String, isFavorite: Boolean)
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
        val etag = scheduleDao.lastEtag()
        val event = api.fetchEvent()
        val partners = api.fetchPartners()
        try {
            val agenda = api.fetchAgenda(etag)
            agenda.second.entries.forEach { entry ->
                entry.value.values.forEach { schedules ->
                    scheduleDao.insertOrUpdateSchedules(event.id, entry.key, schedules)
                }
            }
            scheduleDao.updateEtag(agenda.first)
        } catch (_: Throwable) {
        }
        eventDao.insertEvent(event)
        partnerDao.insertPartners(partners)
    }

    override fun toggleFavoriteFiltering() {
        scheduleDao.toggleFavoriteFiltering()
    }

    override suspend fun insertOrUpdateTicket(barcode: String) {
        val attendee = try {
            val attendee = api.fetchAttendee(barcode)
            attendee
        } catch (ignored: Throwable) {
            null
        }
        val qrCode = qrCodeGenerator.generate(barcode)
        eventDao.updateTicket(qrCode, barcode, attendee)
    }

    override fun scaffoldConfig(): Flow<ScaffoldConfigUi> = featuresDao.fetchFeatures()

    override fun isFavoriteToggled(): Flow<Boolean> = scheduleDao.isFavoriteToggled()
    override fun event(): Flow<EventUi> = eventDao.fetchEvent()
    override fun partners(): Flow<PartnerGroupsUi> = partnerDao.fetchPartners()
    override fun qanda(): Flow<List<QuestionAndResponseUi>> = eventDao.fetchQAndA()
    override fun menus(): Flow<List<MenuItemUi>> = eventDao.fetchMenus()
    override fun coc(): Flow<CoCUi> = eventDao.fetchCoC()
    override fun agenda(date: String): Flow<AgendaUi> = scheduleDao.fetchSchedules(date)
    override fun speaker(speakerId: String): Flow<SpeakerUi> = speakerDao.fetchSpeaker(speakerId)

    override fun markAsRead(scheduleId: String, isFavorite: Boolean) =
        scheduleDao.markAsFavorite(scheduleId, isFavorite)

    override fun scheduleItem(scheduleId: String): TalkUi = talkDao.fetchTalk(scheduleId)
}
