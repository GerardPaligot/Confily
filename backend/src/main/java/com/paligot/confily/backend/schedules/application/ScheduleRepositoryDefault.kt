package com.paligot.confily.backend.schedules.application

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.internals.infrastructure.firestore.CategoryFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.EventFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.FormatFirestore
import com.paligot.confily.backend.internals.infrastructure.firestore.ScheduleItemFirestore
import com.paligot.confily.backend.schedules.domain.ScheduleRepository
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.sessions.convertToModel
import com.paligot.confily.backend.speakers.SpeakerDao
import com.paligot.confily.models.ScheduleItem
import kotlinx.coroutines.coroutineScope

class ScheduleRepositoryDefault(
    private val eventDao: EventFirestore,
    private val sessionDao: SessionDao,
    private val categoryDao: CategoryFirestore,
    private val formatFirestore: FormatFirestore,
    private val speakerDao: SpeakerDao,
    private val scheduleItemFirestore: ScheduleItemFirestore
) : ScheduleRepository {
    override suspend fun get(eventId: String, scheduleId: String): ScheduleItem = coroutineScope {
        val eventDb = eventDao.get(eventId)
        val scheduleItem = scheduleItemFirestore.get(eventId, scheduleId)
            ?: throw NotFoundException("Schedule item $scheduleId not found")
        val talk = if (scheduleItem.talkId != null) {
            val talkDb = sessionDao.getTalkSession(eventId, scheduleItem.talkId)
                ?: throw NotFoundException("Talk ${scheduleItem.talkId} not found")
            val speakers = speakerDao.getByIds(eventId, talkDb.speakerIds)
            val category = categoryDao.get(eventId, talkDb.category)
            val format = formatFirestore.get(eventId, talkDb.format)
            talkDb.convertToModel(speakers, category, format, eventDb)
        } else {
            null
        }
        return@coroutineScope scheduleItem.convertToModel(talk)
    }
}
