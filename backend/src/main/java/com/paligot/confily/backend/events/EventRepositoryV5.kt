package com.paligot.confily.backend.events

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.activities.ActivityDao
import com.paligot.confily.backend.activities.convertToModel
import com.paligot.confily.backend.categories.CategoryDao
import com.paligot.confily.backend.categories.convertToModel
import com.paligot.confily.backend.formats.FormatDao
import com.paligot.confily.backend.formats.convertToModel
import com.paligot.confily.backend.jobs.JobDao
import com.paligot.confily.backend.map.MapDao
import com.paligot.confily.backend.map.MapDb
import com.paligot.confily.backend.map.convertToModel
import com.paligot.confily.backend.partners.PartnerDao
import com.paligot.confily.backend.partners.convertToModelV3
import com.paligot.confily.backend.qanda.QAndADao
import com.paligot.confily.backend.qanda.QAndADb
import com.paligot.confily.backend.qanda.convertToModel
import com.paligot.confily.backend.schedules.ScheduleItemDao
import com.paligot.confily.backend.schedules.convertToEventSession
import com.paligot.confily.backend.schedules.convertToModelV4
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.sessions.convertToModel
import com.paligot.confily.backend.speakers.SpeakerDao
import com.paligot.confily.backend.speakers.convertToModel
import com.paligot.confily.backend.tags.TagDao
import com.paligot.confily.backend.tags.convertToModel
import com.paligot.confily.backend.team.TeamDao
import com.paligot.confily.backend.team.convertToModel
import com.paligot.confily.backend.third.parties.welovedevs.convertToModel
import com.paligot.confily.models.AgendaV4
import com.paligot.confily.models.EventV5
import com.paligot.confily.models.PartnersActivities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@Suppress("LongParameterList")
class EventRepositoryV5(
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val sessionDao: SessionDao,
    private val categoryDao: CategoryDao,
    private val formatDao: FormatDao,
    private val tagDao: TagDao,
    private val scheduleItemDao: ScheduleItemDao,
    private val partnerDao: PartnerDao,
    private val qAndADao: QAndADao,
    private val jobDao: JobDao,
    private val activityDao: ActivityDao,
    private val teamDao: TeamDao,
    private val mapDao: MapDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun get(eventId: String): EventV5 {
        val eventDb = eventDao.get(eventId)
        return eventDao.getEventFile(eventId, eventDb.updatedAt)
            ?: throw NotFoundException("Event $eventId Not Found")
    }

    suspend fun generate(eventId: String) = coroutineScope {
        val eventDb = eventDao.get(eventId)
        val event = buildEvent(eventDb)
        eventDao.uploadEventFile(eventId, eventDb.updatedAt, event)
        return@coroutineScope event
    }

    private suspend fun buildEvent(event: EventDb): EventV5 = coroutineScope {
        val qanda = async(dispatcher) {
            qAndADao.getAll(event.slugId)
                .groupBy { it.language }
                .map { it.key to it.value.sortedBy { it.order }.map(QAndADb::convertToModel) }
                .toMap()
        }
        val maps = async(dispatcher) { mapDao.getAll(event.slugId).map(MapDb::convertToModel) }
        return@coroutineScope event.convertToModelV5(
            qanda = qanda.await(),
            agenda = agenda(event),
            partners = activities(event),
            team = teamMembers(event),
            maps = maps.await()
        )
    }

    private suspend fun agenda(eventDb: EventDb) = coroutineScope {
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

    private suspend fun activities(eventDb: EventDb): PartnersActivities = coroutineScope {
        val partners = async(dispatcher) { partnerDao.getAll(eventDb.slugId) }
        val jobs = async(dispatcher) { jobDao.getAll(eventDb.slugId) }
        val activities = async(dispatcher) { activityDao.getAll(eventDb.slugId) }
        val fetchedJobs = jobs.await()
        return@coroutineScope PartnersActivities(
            types = eventDb.sponsoringTypes,
            partners = partners.await().map { partner ->
                partner.convertToModelV3(
                    fetchedJobs
                        .filter { it.partnerId == partner.id }
                        .map { it.convertToModel() }
                )
            },
            activities = activities.await()
                .sortedBy { it.startTime }
                .map { it.convertToModel() }
        )
    }

    private suspend fun teamMembers(eventDb: EventDb) = coroutineScope {
        val orderMap = eventDb.teamGroups.associate { it.name to it.order }
        return@coroutineScope teamDao.getAll(eventDb.slugId)
            .asSequence()
            .sortedBy { orderMap[it.teamName] ?: 0 }
            .groupBy { it.teamName }
            .filter { it.key != "" }
            .map { entry -> entry.key to entry.value.map { it.convertToModel() } }
            .associate { it }
    }
}
