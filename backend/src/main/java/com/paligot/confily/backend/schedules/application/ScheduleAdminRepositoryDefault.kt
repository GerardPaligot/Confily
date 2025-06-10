package com.paligot.confily.backend.schedules.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.helpers.date.FormatterPattern
import com.paligot.confily.backend.internals.helpers.date.format
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.FormatFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.ScheduleItemFirestore
import com.paligot.confily.backend.schedules.domain.ScheduleAdminRepository
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.models.inputs.ScheduleInput
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime

class ScheduleAdminRepositoryDefault(
    private val eventDao: EventFirestore,
    private val sessionDao: SessionDao,
    private val formatFirestore: FormatFirestore,
    private val scheduleItemFirestore: ScheduleItemFirestore
) : ScheduleAdminRepository {
    override suspend fun create(eventId: String, scheduleInput: ScheduleInput): String =
        coroutineScope {
            val event = eventDao.get(eventId)
            return@coroutineScope if (scheduleInput.talkId == null) {
                val scheduleItem = scheduleInput.convertToEntity(endTime = scheduleInput.endTime!!)
                scheduleItemFirestore.createOrUpdate(eventId, scheduleItem)
                eventDao.updateAgendaUpdatedAt(event)
                scheduleItem.id
            } else {
                val talk = sessionDao.getTalkSession(eventId, scheduleInput.talkId!!)
                    ?: throw NotFoundException("Talk ${scheduleInput.talkId} not found")
                val format = formatFirestore.get(eventId, talk.format)
                    ?: throw NotFoundException("Format ${talk.category} not found")
                val endTime =
                    LocalDateTime.parse(scheduleInput.startTime).plusMinutes(format.time.toLong())
                val scheduleItem = scheduleInput.convertToEntity(
                    endTime = endTime.format(FormatterPattern.Iso8601),
                    talkId = talk.id
                )
                scheduleItemFirestore.createOrUpdate(eventId, scheduleItem)
                eventDao.updateAgendaUpdatedAt(event)
                scheduleItem.id
            }
        }

    override suspend fun delete(eventId: String, scheduleId: String) = coroutineScope {
        scheduleItemFirestore.delete(eventId, scheduleId)
    }
}
