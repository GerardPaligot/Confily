package com.paligot.conferences.backend.schedulers

import com.paligot.conferences.backend.NotFoundException
import com.paligot.conferences.backend.date.FormatterPattern
import com.paligot.conferences.backend.date.format
import com.paligot.conferences.backend.events.EventDao
import com.paligot.conferences.backend.speakers.SpeakerDao
import com.paligot.conferences.backend.talks.TalkDao
import com.paligot.conferences.backend.talks.convertToModel
import com.paligot.conferences.models.inputs.ScheduleInput
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime

class ScheduleRepository(
    private val eventDao: EventDao,
    private val talkDao: TalkDao,
    private val speakerDao: SpeakerDao,
    private val scheduleItemDao: ScheduleItemDao
) {

    suspend fun create(eventId: String, apiKey: String, scheduleInput: ScheduleInput) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        if (scheduleInput.talkId == null) {
            val scheduleItem = scheduleInput.convertToDb(endTime = scheduleInput.endTime!!)
            scheduleItemDao.createOrUpdate(eventId, scheduleItem)
            eventDao.updateUpdatedAt(eventId)
            return@coroutineScope scheduleItem.id
        } else {
            val talk = talkDao.get(eventId, scheduleInput.talkId!!)
                ?: throw NotFoundException("Talk ${scheduleInput.talkId} not found")
            val format = event.formats[talk.format]
                ?: throw NotFoundException("Category ${talk.category} not found in Event")
            val endTime = LocalDateTime.parse(scheduleInput.startTime).plusMinutes(format.toLong())
            val scheduleItem = scheduleInput.convertToDb(endTime = endTime.format(FormatterPattern.Iso8601), talkId = talk.id)
            scheduleItemDao.createOrUpdate(eventId, scheduleItem)
            eventDao.updateUpdatedAt(eventId)
            return@coroutineScope scheduleItem.id
        }
    }

    suspend fun get(eventId: String, scheduleId: String) = coroutineScope {
        val eventDb = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val scheduleItem = scheduleItemDao.get(eventId, scheduleId)
            ?: throw NotFoundException("Schedule item $scheduleId not found")
        val talk = if (scheduleItem.talkId != null) {
            val talkDb = talkDao.get(eventId, scheduleItem.talkId)
                ?: throw NotFoundException("Talk ${scheduleItem.talkId} not found")
            val speakers = speakerDao.getByIds(eventId, *talkDb.speakerIds.toTypedArray())
            talkDb.convertToModel(speakers, eventDb)
        } else null
        eventDao.updateUpdatedAt(eventId)
        return@coroutineScope scheduleItem.convertToModel(talk)
    }

    suspend fun delete(eventId: String, apiKey: String, scheduleId: String) = coroutineScope {
        eventDao.getVerified(eventId, apiKey)
        scheduleItemDao.delete(eventId, scheduleId)
        eventDao.updateUpdatedAt(eventId)
    }
}