package com.paligot.confily.backend.schedules

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.categories.CategoryDao
import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.formats.FormatDao
import com.paligot.confily.backend.internals.date.FormatterPattern
import com.paligot.confily.backend.internals.date.format
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.sessions.convertToModel
import com.paligot.confily.backend.speakers.SpeakerDao
import com.paligot.confily.models.inputs.ScheduleInput
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime

class ScheduleRepository(
    private val eventDao: EventDao,
    private val sessionDao: SessionDao,
    private val categoryDao: CategoryDao,
    private val formatDao: FormatDao,
    private val speakerDao: SpeakerDao,
    private val scheduleItemDao: ScheduleItemDao
) {
    suspend fun create(eventId: String, apiKey: String, scheduleInput: ScheduleInput) =
        coroutineScope {
            val event = eventDao.getVerified(eventId, apiKey)
            return@coroutineScope if (scheduleInput.talkId == null) {
                val scheduleItem = scheduleInput.convertToDb(endTime = scheduleInput.endTime!!)
                scheduleItemDao.createOrUpdate(eventId, scheduleItem)
                eventDao.updateAgendaUpdatedAt(event)
                scheduleItem.id
            } else {
                val talk = sessionDao.getTalkSession(eventId, scheduleInput.talkId!!)
                    ?: throw NotFoundException("Talk ${scheduleInput.talkId} not found")
                val format = formatDao.get(eventId, talk.format)
                    ?: throw NotFoundException("Format ${talk.category} not found")
                val endTime =
                    LocalDateTime.parse(scheduleInput.startTime).plusMinutes(format.time.toLong())
                val scheduleItem = scheduleInput.convertToDb(
                    endTime = endTime.format(FormatterPattern.Iso8601),
                    talkId = talk.id
                )
                scheduleItemDao.createOrUpdate(eventId, scheduleItem)
                eventDao.updateAgendaUpdatedAt(event)
                scheduleItem.id
            }
        }

    suspend fun get(eventId: String, scheduleId: String) = coroutineScope {
        val eventDb = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val scheduleItem = scheduleItemDao.get(eventId, scheduleId)
            ?: throw NotFoundException("Schedule item $scheduleId not found")
        val talk = if (scheduleItem.talkId != null) {
            val talkDb = sessionDao.getTalkSession(eventId, scheduleItem.talkId)
                ?: throw NotFoundException("Talk ${scheduleItem.talkId} not found")
            val speakers = speakerDao.getByIds(eventId, talkDb.speakerIds)
            val category = categoryDao.get(eventId, talkDb.category)
            val format = formatDao.get(eventId, talkDb.format)
            talkDb.convertToModel(speakers, category, format, eventDb)
        } else {
            null
        }
        return@coroutineScope scheduleItem.convertToModel(talk)
    }

    suspend fun delete(eventId: String, scheduleId: String) = coroutineScope {
        scheduleItemDao.delete(eventId, scheduleId)
    }
}
