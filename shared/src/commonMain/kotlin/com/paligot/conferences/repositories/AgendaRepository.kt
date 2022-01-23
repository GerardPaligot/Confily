package com.paligot.conferences.repositories

import com.paligot.conferences.database.EventDao
import com.paligot.conferences.database.ScheduleDao
import com.paligot.conferences.database.SpeakerDao
import com.paligot.conferences.database.TalkDao
import com.paligot.conferences.network.ConferenceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

interface AgendaRepository {
    suspend fun fetchAndStoreAgenda()
    suspend fun event(): EventUi
    suspend fun agenda(): Flow<AgendaUi>
    suspend fun markAsRead(scheduleId: String, isFavorite: Boolean)
    suspend fun scheduleItem(scheduleId: String): TalkUi
    suspend fun speaker(speakerId: String): SpeakerUi

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

    override suspend fun event(): EventUi = eventDao.fetchEvent()
    override suspend fun agenda(): Flow<AgendaUi> = scheduleDao.fetchSchedules().transform { talks ->
        emit(AgendaUi(talks = talks.groupBy { it.time }))
    }

    override suspend fun markAsRead(scheduleId: String, isFavorite: Boolean) =
        scheduleDao.markAsFavorite(scheduleId, isFavorite)

    override suspend fun scheduleItem(scheduleId: String): TalkUi = talkDao.fetchTalk(scheduleId)
    override suspend fun speaker(speakerId: String): SpeakerUi = speakerDao.fetchSpeaker(speakerId)
}
