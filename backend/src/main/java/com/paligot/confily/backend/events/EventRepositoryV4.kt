package com.paligot.confily.backend.events

import com.paligot.confily.backend.categories.CategoryDao
import com.paligot.confily.backend.categories.convertToModel
import com.paligot.confily.backend.formats.FormatDao
import com.paligot.confily.backend.formats.convertToModel
import com.paligot.confily.backend.partners.PartnerDao
import com.paligot.confily.backend.qanda.QAndADao
import com.paligot.confily.backend.schedules.ScheduleItemDao
import com.paligot.confily.backend.schedules.convertToEventSession
import com.paligot.confily.backend.schedules.convertToModelV4
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.sessions.convertToModel
import com.paligot.confily.backend.speakers.SpeakerDao
import com.paligot.confily.backend.speakers.convertToModel
import com.paligot.confily.backend.tags.TagDao
import com.paligot.confily.backend.tags.convertToModel
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.EventV4
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@Suppress("LongParameterList")
class EventRepositoryV4(
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val sessionDao: SessionDao,
    private val categoryDao: CategoryDao,
    private val formatDao: FormatDao,
    private val tagDao: TagDao,
    private val scheduleItemDao: ScheduleItemDao,
    private val partnerDao: PartnerDao,
    private val qAndADao: QAndADao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getV4(eventId: String): EventV4 = coroutineScope {
        val event = eventDao.get(eventId)
        val hasPartners = async { partnerDao.hasPartners(eventId) }
        val qanda = async { qAndADao.hasQAndA(eventId) }
        return@coroutineScope event.convertToModelV4(
            hasPartnerList = hasPartners.await(),
            hasQandA = qanda.await()
        )
    }

    suspend fun agenda(eventDb: EventDb): AgendaV4 =
        eventDao.getAgendaFile(eventDb.slugId, eventDb.agendaUpdatedAt) ?: buildAgenda(eventDb)

    suspend fun generateAgenda(eventId: String) = coroutineScope {
        val eventDb = eventDao.get(eventId)
        val agenda = buildAgenda(eventDb)
        eventDao.uploadAgendaFile(eventId, eventDb.agendaUpdatedAt, agenda)
        return@coroutineScope agenda
    }

    private suspend fun buildAgenda(eventDb: EventDb) = coroutineScope {
        val schedules = async(context = dispatcher) {
            scheduleItemDao.getAll(eventDb.slugId).map { it.convertToModelV4() }
        }.await()
        // For older event, get their break sessions
        val breaks = schedules
            .filter { it.id.contains("-pause") }
            .map { it.convertToEventSession() }
        val sessions = async(context = dispatcher) {
            sessionDao.getAll(eventDb.slugId).map { it.convertToModel(eventDb) }
        }
        val formats = async(context = dispatcher) {
            formatDao.getAll(eventDb.slugId).map { it.convertToModel() }
        }
        val categories = async(context = dispatcher) {
            categoryDao.getAll(eventDb.slugId).map { it.convertToModel() }
        }
        val tags = async(context = dispatcher) {
            tagDao.getAll(eventDb.slugId).map { it.convertToModel() }
        }
        val speakers = async(context = dispatcher) {
            speakerDao.getAll(eventDb.slugId).map { it.convertToModel() }
        }
        return@coroutineScope AgendaV4(
            schedules = schedules,
            sessions = sessions.await() + breaks,
            formats = formats.await(),
            categories = categories.await(),
            tags = tags.await(),
            speakers = speakers.await()
        )
    }
}
