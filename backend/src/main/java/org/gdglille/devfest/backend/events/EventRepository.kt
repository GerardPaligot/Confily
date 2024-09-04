package org.gdglille.devfest.backend.events

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
import org.gdglille.devfest.backend.NotAcceptableException
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.categories.CategoryDao
import org.gdglille.devfest.backend.categories.CategoryDb
import org.gdglille.devfest.backend.formats.FormatDao
import org.gdglille.devfest.backend.formats.FormatDb
import org.gdglille.devfest.backend.internals.date.FormatterPattern
import org.gdglille.devfest.backend.internals.date.format
import org.gdglille.devfest.backend.partners.PartnerDao
import org.gdglille.devfest.backend.partners.Sponsorship
import org.gdglille.devfest.backend.partners.convertToModel
import org.gdglille.devfest.backend.qanda.QAndADao
import org.gdglille.devfest.backend.schedules.ScheduleDb
import org.gdglille.devfest.backend.schedules.ScheduleItemDao
import org.gdglille.devfest.backend.schedules.convertToModel
import org.gdglille.devfest.backend.schedules.convertToPlanningEventModel
import org.gdglille.devfest.backend.schedules.convertToPlanningTalkModel
import org.gdglille.devfest.backend.sessions.EventSessionDb
import org.gdglille.devfest.backend.sessions.SessionDao
import org.gdglille.devfest.backend.sessions.SessionDb
import org.gdglille.devfest.backend.sessions.TalkDb
import org.gdglille.devfest.backend.sessions.convertToModel
import org.gdglille.devfest.backend.sessions.convertToModelInfo
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.speakers.SpeakerDb
import org.gdglille.devfest.backend.third.parties.geocode.GeocodeApi
import org.gdglille.devfest.backend.third.parties.geocode.convertToDb
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
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
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

    suspend fun create(eventInput: com.paligot.confily.models.inputs.CreatingEventInput, language: String) = coroutineScope {
        val addressDb = geocodeApi.geocode(eventInput.address).convertToDb()
            ?: throw NotAcceptableException("Your address information isn't found")
        val event = eventInput.convertToDb(addressDb, language)
        eventDao.createOrUpdate(event)
        return@coroutineScope CreatedEvent(eventId = event.slugId, apiKey = event.apiKey)
    }

    suspend fun update(eventId: String, apiKey: String, eventInput: EventInput) = coroutineScope {
        val addressDb = geocodeApi.geocode(eventInput.address).convertToDb()
            ?: throw NotAcceptableException("Your address information isn't found")
        val event = eventDao.getVerified(eventId, apiKey)
        eventDao.createOrUpdate(eventInput.convertToDb(event, addressDb))
        return@coroutineScope eventId
    }

    suspend fun updateMenus(eventId: String, apiKey: String, menus: List<LunchMenuInput>) =
        coroutineScope {
            eventDao.updateMenus(eventId, apiKey, menus.map { it.convertToDb() })
            return@coroutineScope eventId
        }

    suspend fun updateCoC(eventId: String, apiKey: String, coc: CoCInput) = coroutineScope {
        eventDao.updateCoc(eventId, apiKey, coc.coc)
        return@coroutineScope eventId
    }

    suspend fun updateFeatures(eventId: String, apiKey: String, features: FeaturesActivatedInput) =
        coroutineScope {
            eventDao.updateFeatures(eventId, apiKey, features.hasNetworking)
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
