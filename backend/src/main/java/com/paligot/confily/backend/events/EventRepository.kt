package com.paligot.confily.backend.events

import com.paligot.confily.backend.NotAcceptableException
import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.categories.CategoryDao
import com.paligot.confily.backend.categories.CategoryDb
import com.paligot.confily.backend.formats.FormatDao
import com.paligot.confily.backend.formats.FormatDb
import com.paligot.confily.backend.internals.date.FormatterPattern
import com.paligot.confily.backend.internals.date.format
import com.paligot.confily.backend.partners.PartnerDao
import com.paligot.confily.backend.partners.Sponsorship
import com.paligot.confily.backend.partners.convertToModel
import com.paligot.confily.backend.qanda.QAndADao
import com.paligot.confily.backend.schedules.ScheduleDb
import com.paligot.confily.backend.schedules.ScheduleItemDao
import com.paligot.confily.backend.schedules.convertToModel
import com.paligot.confily.backend.schedules.convertToPlanningEventModel
import com.paligot.confily.backend.schedules.convertToPlanningTalkModel
import com.paligot.confily.backend.sessions.EventSessionDb
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.sessions.SessionDb
import com.paligot.confily.backend.sessions.TalkDb
import com.paligot.confily.backend.sessions.convertToModel
import com.paligot.confily.backend.sessions.convertToModelInfo
import com.paligot.confily.backend.speakers.SpeakerDao
import com.paligot.confily.backend.speakers.SpeakerDb
import com.paligot.confily.backend.third.parties.geocode.GeocodeApi
import com.paligot.confily.backend.third.parties.geocode.convertToDb
import com.paligot.confily.models.Agenda
import com.paligot.confily.models.CreatedEvent
import com.paligot.confily.models.Event
import com.paligot.confily.models.EventList
import com.paligot.confily.models.EventPartners
import com.paligot.confily.models.PlanningItem
import com.paligot.confily.models.inputs.CoCInput
import com.paligot.confily.models.inputs.CreatingEventInput
import com.paligot.confily.models.inputs.EventInput
import com.paligot.confily.models.inputs.FeaturesActivatedInput
import com.paligot.confily.models.inputs.LunchMenuInput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime

@Suppress("LongParameterList")
class EventRepository(
    private val geocodeApi: GeocodeApi,
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val qAndADao: QAndADao,
    private val sessionDao: SessionDao,
    private val categoryDao: CategoryDao,
    private val formatDao: FormatDao,
    private val scheduleItemDao: ScheduleItemDao,
    private val partnerDao: PartnerDao
) {
    suspend fun list(): EventList {
        val events = eventDao.list().map { it.convertToEventItemList() }
        val now = LocalDateTime.now()
        return EventList(
            future = events
                .filter { LocalDateTime.parse(it.endDate.dropLast(1)).isAfter(now) }
                .sortedBy { it.startDate },
            past = events
                .filter { LocalDateTime.parse(it.startDate.dropLast(1)).isBefore(now) }
                .sortedBy { it.endDate }
        )
    }

    suspend fun getWithPartners(eventId: String): Event = coroutineScope {
        val event = eventDao.get(eventId)
        val partners = partnerDao.getAll(eventId)
        val qanda = qAndADao.getAll(eventId, event.defaultLanguage)
        return@coroutineScope event.convertToModel(
            EventPartners(
                golds = partners.filter { it.sponsoring == Sponsorship.Gold.name }
                    .map { it.convertToModel() }
                    .sortedBy { it.name },
                silvers = partners.filter { it.sponsoring == Sponsorship.Silver.name }
                    .map { it.convertToModel() }
                    .sortedBy { it.name },
                bronzes = partners.filter { it.sponsoring == Sponsorship.Bronze.name }
                    .map { it.convertToModel() }
                    .sortedBy { it.name },
                others = partners.filter { it.sponsoring == Sponsorship.Other.name }
                    .map { it.convertToModel() }
                    .sortedBy { it.name }
            ),
            qanda
        )
    }

    suspend fun create(eventInput: CreatingEventInput, language: String) = coroutineScope {
        val addressDb = geocodeApi.geocode(eventInput.address).convertToDb()
            ?: throw NotAcceptableException("Your address information isn't found")
        val event = eventInput.convertToDb(addressDb, language)
        eventDao.createOrUpdate(event)
        return@coroutineScope CreatedEvent(eventId = event.slugId, apiKey = event.apiKey)
    }

    suspend fun update(eventId: String, eventInput: EventInput) = coroutineScope {
        val addressDb = geocodeApi.geocode(eventInput.address).convertToDb()
            ?: throw NotAcceptableException("Your address information isn't found")
        val event = eventDao.get(eventId)
        eventDao.createOrUpdate(eventInput.convertToDb(event, addressDb))
        return@coroutineScope eventId
    }

    suspend fun updateMenus(eventId: String, menus: List<LunchMenuInput>) =
        coroutineScope {
            eventDao.updateMenus(eventId, menus.map { it.convertToDb() })
            return@coroutineScope eventId
        }

    suspend fun updateCoC(eventId: String, coc: CoCInput) = coroutineScope {
        eventDao.updateCoc(eventId, coc.coc)
        return@coroutineScope eventId
    }

    suspend fun updateFeatures(eventId: String, features: FeaturesActivatedInput) =
        coroutineScope {
            eventDao.updateFeatures(eventId, features.hasNetworking)
            return@coroutineScope eventId
        }

    suspend fun agenda(eventDb: EventDb) = coroutineScope {
        val categories = categoryDao.getAll(eventDb.slugId)
        val formats = formatDao.getAll(eventDb.slugId)
        val speakers = speakerDao.getAll(eventDb.slugId)
        val schedules = scheduleItemDao
            .getAll(eventDb.slugId)
            .groupBy { it.startTime }
            .entries.map {
                async {
                    val scheduleItems = it.value.map {
                        async {
                            if (it.talkId == null) {
                                it.convertToModel(null)
                            } else {
                                val talk = sessionDao.getTalkSession(eventDb.slugId, it.talkId)
                                    ?: return@async it.convertToModel(null)
                                it.convertToModel(
                                    talk.convertToModel(
                                        speakers.filter { talk.speakerIds.contains(it.id) },
                                        categories.find { it.id == talk.category },
                                        formats.find { it.id == talk.format },
                                        eventDb
                                    )
                                )
                            }
                        }
                    }.awaitAll()
                    val key = LocalDateTime.parse(it.key).format(FormatterPattern.HoursMinutes)
                    return@async key to scheduleItems.sortedBy { it.room }
                }
            }.awaitAll().associate { it }.toSortedMap()
        return@coroutineScope Agenda(talks = schedules)
    }

    suspend fun planning(eventDb: EventDb): Map<String, Map<String, List<PlanningItem>>> = coroutineScope {
        val sessions = sessionDao.getAll(eventDb.slugId)
        val speakers = speakerDao.getAll(eventDb.slugId)
        val categories = categoryDao.getAll(eventDb.slugId)
        val formats = formatDao.getAll(eventDb.slugId)
        return@coroutineScope scheduleItemDao.getAll(eventDb.slugId)
            .groupBy { LocalDateTime.parse(it.startTime).format(FormatterPattern.YearMonthDay) }
            .entries.map { schedulesByDay ->
                schedulesByDay(schedulesByDay, sessions, speakers, categories, formats, eventDb)
            }
            .awaitAll()
            .sortedBy { it.first }
            .associate { it }
            .toMap()
    }

    @Suppress("LongParameterList")
    private fun CoroutineScope.schedulesByDay(
        schedulesByDay: Map.Entry<String, List<ScheduleDb>>,
        sessions: List<SessionDb>,
        speakers: List<SpeakerDb>,
        categories: List<CategoryDb>,
        formats: List<FormatDb>,
        eventDb: EventDb
    ) = async {
        return@async schedulesByDay.key to schedulesByDay.value
            .groupBy { LocalDateTime.parse(it.startTime).format(FormatterPattern.HoursMinutes) }
            .entries.map { schedulesBySlot ->
                schedulesBySlot.key to schedulesBySlot.value
                    .map { schedule ->
                        schedulesBySlot(schedule, sessions, speakers, categories, formats, eventDb)
                    }
                    .awaitAll()
                    .sortedBy { it.order }
            }
            .sortedBy { it.first }
            .associate { it }
            .toMap()
    }

    @Suppress("LongParameterList")
    private fun CoroutineScope.schedulesBySlot(
        schedule: ScheduleDb,
        sessions: List<SessionDb>,
        speakers: List<SpeakerDb>,
        categories: List<CategoryDb>,
        formats: List<FormatDb>,
        eventDb: EventDb
    ) = async {
        val session = sessions.find { it.id == schedule.talkId }
            ?: throw NotFoundException("Session ${schedule.id} not found")
        when (session) {
            is EventSessionDb -> schedule.convertToPlanningEventModel(session.convertToModelInfo())

            is TalkDb -> {
                val speakersTalk = speakers.filter { session.speakerIds.contains(it.id) }
                val category = categories.find { it.id == session.category }
                val format = formats.find { it.id == session.format }
                schedule.convertToPlanningTalkModel(
                    session.convertToModel(speakersTalk, category, format, eventDb)
                )
            }
        }
    }
}
