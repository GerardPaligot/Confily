package org.gdglille.devfest.backend.events

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotAcceptableException
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.categories.CategoryDao
import org.gdglille.devfest.backend.formats.FormatDao
import org.gdglille.devfest.backend.internals.date.FormatterPattern
import org.gdglille.devfest.backend.internals.date.format
import org.gdglille.devfest.backend.partners.PartnerDao
import org.gdglille.devfest.backend.partners.Sponsorship
import org.gdglille.devfest.backend.partners.convertToModel
import org.gdglille.devfest.backend.qanda.QAndADao
import org.gdglille.devfest.backend.schedulers.ScheduleItemDao
import org.gdglille.devfest.backend.schedulers.convertToModel
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.talks.TalkDao
import org.gdglille.devfest.backend.talks.convertToModel
import org.gdglille.devfest.backend.third.parties.geocode.GeocodeApi
import org.gdglille.devfest.backend.third.parties.geocode.convertToDb
import org.gdglille.devfest.backend.third.parties.openplanner.OpenPlannerApi
import org.gdglille.devfest.backend.third.parties.openplanner.convertToDb
import org.gdglille.devfest.backend.third.parties.openplanner.convertToScheduleDb
import org.gdglille.devfest.backend.third.parties.openplanner.convertToTalkDb
import org.gdglille.devfest.models.Agenda
import org.gdglille.devfest.models.CreatedEvent
import org.gdglille.devfest.models.Event
import org.gdglille.devfest.models.EventList
import org.gdglille.devfest.models.EventPartners
import org.gdglille.devfest.models.inputs.CoCInput
import org.gdglille.devfest.models.inputs.CreatingEventInput
import org.gdglille.devfest.models.inputs.EventInput
import org.gdglille.devfest.models.inputs.FeaturesActivatedInput
import org.gdglille.devfest.models.inputs.LunchMenuInput
import java.time.LocalDateTime

@Suppress("LongParameterList")
class EventRepository(
    private val geocodeApi: GeocodeApi,
    private val openPlannerApi: OpenPlannerApi,
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val qAndADao: QAndADao,
    private val talkDao: TalkDao,
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

    suspend fun create(eventInput: CreatingEventInput, language: String) = coroutineScope {
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
                            if (it.talkId == null) it.convertToModel(null)
                            else {
                                val talk = talkDao.get(eventDb.slugId, it.talkId)
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

    suspend fun openPlanner(eventId: String, apiKey: String) =
        coroutineScope {
            val event = eventDao.getVerified(eventId, apiKey)
            val config = event.openPlannerConfig
                ?: throw NotAcceptableException("OpenPlanner config not initialized")
            val openPlanner = openPlannerApi.fetchPrivateJson(config.eventId, config.privateId)
            openPlanner.event.categories
                .map { async { categoryDao.createOrUpdate(eventId, it.convertToDb()) } }
                .awaitAll()
            openPlanner.event.formats
                .map { async { formatDao.createOrUpdate(eventId, it.convertToDb()) } }
                .awaitAll()
            val allSpeakers = openPlanner.sessions
                .map { it.speakerIds }.flatten()
            openPlanner.speakers
                .filter { allSpeakers.contains(it.id) }
                .map { async { speakerDao.createOrUpdate(eventId, it.convertToDb()) } }
                .awaitAll()
            openPlanner.sessions
                .map { async { talkDao.createOrUpdate(eventId, it.convertToTalkDb()) } }
            openPlanner.sessions
                .filter { it.trackId != null && it.dateStart != null && it.dateEnd != null }
                .groupBy { it.dateStart }
                .map {
                    async {
                        it.value.forEachIndexed { index, sessionOP ->
                            scheduleItemDao.createOrUpdate(
                                eventId,
                                sessionOP.convertToScheduleDb(index, openPlanner.event.tracks)
                            )
                        }
                    }
                }
                .awaitAll()
            eventDao.updateAgendaUpdatedAt(event)
        }
}
