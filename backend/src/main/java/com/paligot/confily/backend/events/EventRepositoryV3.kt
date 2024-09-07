package com.paligot.confily.backend.events

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.categories.CategoryDao
import com.paligot.confily.backend.categories.convertToModel
import com.paligot.confily.backend.formats.FormatDao
import com.paligot.confily.backend.formats.convertToModel
import com.paligot.confily.backend.partners.PartnerDao
import com.paligot.confily.backend.qanda.QAndADao
import com.paligot.confily.backend.schedules.ScheduleItemDao
import com.paligot.confily.backend.schedules.convertToModelV3
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.sessions.convertToModel
import com.paligot.confily.backend.speakers.SpeakerDao
import com.paligot.confily.backend.speakers.convertToModel
import com.paligot.confily.models.AgendaV3
import com.paligot.confily.models.EventV3
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@Suppress("LongParameterList")
class EventRepositoryV3(
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val sessionDao: SessionDao,
    private val categoryDao: CategoryDao,
    private val formatDao: FormatDao,
    private val scheduleItemDao: ScheduleItemDao,
    private val partnerDao: PartnerDao,
    private val qAndADao: QAndADao
) {
    suspend fun getV3(eventId: String): EventV3 = coroutineScope {
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val hasPartners = async { partnerDao.hasPartners(eventId) }
        val qanda = async { qAndADao.hasQAndA(eventId) }
        return@coroutineScope event.convertToModelV3(
            hasPartnerList = hasPartners.await(),
            hasQandA = qanda.await()
        )
    }

    suspend fun agenda(eventDb: EventDb) = coroutineScope {
        return@coroutineScope AgendaV3(
            sessions = async {
                scheduleItemDao.getAll(eventDb.slugId).map { it.convertToModelV3() }
            }.await(),
            talks = async {
                sessionDao.getAllTalkSessions(eventDb.slugId).map { it.convertToModel(eventDb) }
            }.await(),
            formats = async {
                formatDao.getAll(eventDb.slugId).map { it.convertToModel() }
            }.await(),
            categories = async {
                categoryDao.getAll(eventDb.slugId).map { it.convertToModel() }
            }.await(),
            speakers = async {
                speakerDao.getAll(eventDb.slugId).map { it.convertToModel() }
            }.await()
        )
    }
}
