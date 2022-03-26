package com.paligot.conferences.backend.events

import com.paligot.conferences.backend.NotFoundException
import com.paligot.conferences.backend.partners.PartnerDao
import com.paligot.conferences.backend.partners.Sponsorship
import com.paligot.conferences.backend.schedulers.ScheduleItemDao
import com.paligot.conferences.backend.schedulers.convertToModel
import com.paligot.conferences.backend.speakers.SpeakerDao
import com.paligot.conferences.backend.talks.TalkDao
import com.paligot.conferences.backend.talks.convertToModel
import com.paligot.conferences.models.Agenda
import com.paligot.conferences.models.inputs.EventInput
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class EventRepository(
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val talkDao: TalkDao,
    private val scheduleItemDao: ScheduleItemDao,
    private val partnerDao: PartnerDao
) {
    suspend fun list(eventId: String) = coroutineScope {
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        return@coroutineScope event.convertToModel(
            golds = partnerDao.listValidated(event.year, Sponsorship.Gold),
            silvers = partnerDao.listValidated(event.year, Sponsorship.Silver),
            bronzes = partnerDao.listValidated(event.year, Sponsorship.Bronze),
            others = emptyList()
        )
    }

    suspend fun update(eventId: String, apiKey: String, eventInput: EventInput) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        eventDao.createOrUpdate(eventInput.convertToDb(event, apiKey))
        return@coroutineScope eventId
    }

    suspend fun agenda(eventId: String) = coroutineScope {
        val schedules = scheduleItemDao.getAll(eventId).groupBy { it.time }.entries.map {
            async {
                val scheduleItems = it.value.map {
                    async {
                        if (it.talkId == null) it.convertToModel(null)
                        else {
                            val talk = talkDao.get(eventId, it.talkId) ?: return@async it.convertToModel(null)
                            it.convertToModel(
                                talk.convertToModel(speakerDao.getByIds(eventId, *talk.speakerIds.toTypedArray()))
                            )
                        }
                    }
                }.awaitAll()
                return@async it.key to scheduleItems
            }
        }.awaitAll().associate { it }.toSortedMap()
        return@coroutineScope Agenda(talks = schedules)
    }
}