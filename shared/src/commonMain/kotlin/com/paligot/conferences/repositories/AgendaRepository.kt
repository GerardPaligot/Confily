package com.paligot.conferences.repositories

import com.paligot.conferences.models.Agenda
import com.paligot.conferences.models.Event
import com.paligot.conferences.models.ScheduleItem
import com.paligot.conferences.models.Speaker
import com.paligot.conferences.models.Talk
import com.paligot.conferences.network.ConferenceApi

interface AgendaRepository {
    suspend fun event(): Event
    suspend fun agenda(): Agenda
    suspend fun talk(talkId: String): Talk
    suspend fun scheduleItem(scheduleId: String): ScheduleItem
    suspend fun speaker(speakerId: String): Speaker

    object Factory {
        fun create(api: ConferenceApi): AgendaRepository = AgendaRepositoryImpl(api)
    }
}

class AgendaRepositoryImpl(private val api: ConferenceApi) : AgendaRepository {
    override suspend fun event(): Event = api.fetchEvent()
    override suspend fun agenda(): Agenda = api.fetchAgenda()
    override suspend fun talk(talkId: String): Talk = api.fetchTalk(talkId)
    override suspend fun scheduleItem(scheduleId: String): ScheduleItem = api.fetchSchedule(scheduleId)
    override suspend fun speaker(speakerId: String): Speaker = api.fetchSpeaker(speakerId)
}
