package org.gdglille.devfest.repositories

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import org.gdglille.devfest.database.EventDao
import org.gdglille.devfest.database.ScheduleDao
import org.gdglille.devfest.database.SpeakerDao
import org.gdglille.devfest.database.TalkDao
import org.gdglille.devfest.models.AgendaUi
import org.gdglille.devfest.models.EventUi
import org.gdglille.devfest.models.SpeakerUi
import org.gdglille.devfest.models.TalkUi
import org.gdglille.devfest.network.ConferenceApi

interface AgendaRepository {
    suspend fun fetchAndStoreAgenda()
    suspend fun event(): Flow<EventUi>
    suspend fun agenda(): Flow<AgendaUi>
    suspend fun markAsRead(scheduleId: String, isFavorite: Boolean)
    suspend fun scheduleItem(scheduleId: String): TalkUi
    suspend fun speaker(speakerId: String): SpeakerUi

    // Kotlin/Native client
    fun startCollectAgenda(success: (AgendaUi) -> Unit)
    fun stopCollectAgenda()

    object Factory {
        fun create(
            api: ConferenceApi,
            scheduleDao: ScheduleDao,
            speakerDao: SpeakerDao,
            talkDao: TalkDao,
            eventDao: EventDao
        ): AgendaRepository = AgendaRepositoryImpl(api, scheduleDao, speakerDao, talkDao, eventDao)
    }
}

class AgendaRepositoryImpl(
    private val api: ConferenceApi,
    private val scheduleDao: ScheduleDao,
    private val speakerDao: SpeakerDao,
    private val talkDao: TalkDao,
    private val eventDao: EventDao
) : AgendaRepository {
    override suspend fun fetchAndStoreAgenda() {
        val event = api.fetchEvent()
        val agenda = api.fetchAgenda()
        agenda.talks.values.forEach { schedules ->
            scheduleDao.insertOrUpdateSchedules(event.id, schedules)
        }
        eventDao.insertEvent(event)
    }

    override suspend fun event(): Flow<EventUi> = eventDao.fetchEvent()
    override suspend fun agenda(): Flow<AgendaUi> = scheduleDao.fetchSchedules().transform { talks ->
        emit(AgendaUi(talks = talks.groupBy { it.time }.mapValues { entry -> entry.value.sortedBy { it.room } }))
    }

    override suspend fun markAsRead(scheduleId: String, isFavorite: Boolean) =
        scheduleDao.markAsFavorite(scheduleId, isFavorite)

    override suspend fun scheduleItem(scheduleId: String): TalkUi = talkDao.fetchTalk(scheduleId)
    override suspend fun speaker(speakerId: String): SpeakerUi = speakerDao.fetchSpeaker(speakerId)

    private val coroutineScope: CoroutineScope = MainScope()
    var agendaJob: Job? = null
    override fun startCollectAgenda(success: (AgendaUi) -> Unit) {
        agendaJob = coroutineScope.launch {
            agenda().collect {
                success(it)
            }
        }
    }

    override fun stopCollectAgenda() {
        agendaJob?.cancel()
    }
}
