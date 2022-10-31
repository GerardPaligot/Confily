package org.gdglille.devfest.repositories

import com.russhwolf.settings.ExperimentalSettingsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.database.FeaturesActivatedDao
import org.gdglille.devfest.database.ScheduleDao
import org.gdglille.devfest.database.SpeakerDao
import org.gdglille.devfest.database.TalkDao
import org.gdglille.devfest.database.UserDao
import org.gdglille.devfest.models.AgendaUi
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
    suspend fun isFavoriteToggled(): Flow<Boolean>
    suspend fun toggleFavoriteFiltering()
    suspend fun insertOrUpdateTicket(barcode: String)
    suspend fun scaffoldConfig(): Flow<ScaffoldConfigUi>
    suspend fun event(): Flow<EventUi>
    suspend fun partners(): Flow<PartnerGroupsUi>
    suspend fun qanda(): Flow<List<QuestionAndResponseUi>>
    suspend fun menus(): Flow<List<MenuItemUi>>
    suspend fun coc(): Flow<String>
    suspend fun agenda(date: String): Flow<AgendaUi>
    suspend fun markAsRead(scheduleId: String, isFavorite: Boolean)
    suspend fun scheduleItem(scheduleId: String): TalkUi
    suspend fun speaker(speakerId: String): Flow<SpeakerUi>

    // Kotlin/Native client
    fun startCollectAgenda(date: String, success: (AgendaUi) -> Unit, failure: (Throwable) -> Unit)
    fun stopCollectAgenda()
    fun startCollectEvent(success: (EventUi) -> Unit, failure: (Throwable) -> Unit)
    fun stopCollectEvent()
    fun startCollectPartners(success: (PartnerGroupsUi) -> Unit, failure: (Throwable) -> Unit)
    fun stopCollectPartners()
    fun startCollectMenus(success: (List<MenuItemUi>) -> Unit, failure: (Throwable) -> Unit)
    fun stopCollectMenus()
    fun startCollectSpeaker(speakerId: String, success: (SpeakerUi) -> Unit, failure: (Throwable) -> Unit)
    fun stopCollectSpeaker()

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
            userDao: UserDao,
            featuresDao: FeaturesActivatedDao,
            qrCodeGenerator: QrCodeGenerator
        ): AgendaRepository = AgendaRepositoryImpl(
            api,
            scheduleDao,
            speakerDao,
            talkDao,
            eventDao,
            userDao,
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
    private val userDao: UserDao,
    private val featuresDao: FeaturesActivatedDao,
    private val qrCodeGenerator: QrCodeGenerator
) : AgendaRepository {
    override suspend fun fetchAndStoreAgenda() {
        val etag = scheduleDao.lastEtag()
        val event = api.fetchEvent()
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
    }

    override suspend fun isFavoriteToggled(): Flow<Boolean> = scheduleDao.isFavoriteToggled()

    override suspend fun toggleFavoriteFiltering() {
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

    override suspend fun scaffoldConfig(): Flow<ScaffoldConfigUi> = featuresDao.fetchFeatures()
    override suspend fun event(): Flow<EventUi> = eventDao.fetchEvent()
    override suspend fun partners(): Flow<PartnerGroupsUi> = eventDao.fetchPartners()
    override suspend fun qanda(): Flow<List<QuestionAndResponseUi>> = eventDao.fetchQAndA()
    override suspend fun menus(): Flow<List<MenuItemUi>> = eventDao.fetchMenus()
    override suspend fun coc(): Flow<String> = eventDao.fetchCoC()

    override suspend fun agenda(date: String): Flow<AgendaUi> = scheduleDao.fetchSchedules(date)

    override suspend fun markAsRead(scheduleId: String, isFavorite: Boolean) =
        scheduleDao.markAsFavorite(scheduleId, isFavorite)

    override suspend fun scheduleItem(scheduleId: String): TalkUi = talkDao.fetchTalk(scheduleId)
    override suspend fun speaker(speakerId: String): Flow<SpeakerUi> = speakerDao.fetchSpeaker(speakerId)

    private val coroutineScope: CoroutineScope = MainScope()
    private var agendaJob: Job? = null
    override fun startCollectAgenda(
        date: String,
        success: (AgendaUi) -> Unit,
        failure: (Throwable) -> Unit
    ) {
        agendaJob = coroutineScope.launch {
            try {
                agenda(date = date).collect {
                    success(it)
                }
            } catch (throwable: Throwable) {
                failure(throwable)
            }
        }
    }

    override fun stopCollectAgenda() {
        agendaJob?.cancel()
    }

    private var eventJob: Job? = null
    override fun startCollectEvent(success: (EventUi) -> Unit, failure: (Throwable) -> Unit) {
        eventJob = coroutineScope.launch {
            try {
                event().collect {
                    success(it)
                }
            } catch (throwable: Throwable) {
                failure(throwable)
            }
        }
    }

    override fun stopCollectEvent() {
        eventJob?.cancel()
    }

    private var partnersJob: Job? = null
    override fun startCollectPartners(success: (PartnerGroupsUi) -> Unit, failure: (Throwable) -> Unit) {
        partnersJob = coroutineScope.launch {
            try {
                partners().collect {
                    success(it)
                }
            } catch (throwable: Throwable) {
                failure(throwable)
            }
        }
    }

    override fun stopCollectPartners() {
        partnersJob?.cancel()
    }

    private var menusJob: Job? = null
    override fun startCollectMenus(success: (List<MenuItemUi>) -> Unit, failure: (Throwable) -> Unit) {
        menusJob = coroutineScope.launch {
            try {
                menus().collect {
                    success(it)
                }
            } catch (throwable: Throwable) {
                failure(throwable)
            }
        }
    }

    override fun stopCollectMenus() {
        menusJob?.cancel()
    }

    private var speakerJob: Job? = null
    override fun startCollectSpeaker(speakerId: String, success: (SpeakerUi) -> Unit, failure: (Throwable) -> Unit) {
        speakerJob = coroutineScope.launch {
            try {
                speaker(speakerId).collect {
                    success(it)
                }
            } catch (throwable: Throwable) {
                failure(throwable)
            }
        }
    }

    override fun stopCollectSpeaker() {
        speakerJob?.cancel()
    }
}
