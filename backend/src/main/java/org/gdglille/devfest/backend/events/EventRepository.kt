package org.gdglille.devfest.backend.events

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotAcceptableException
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.internals.date.FormatterPattern
import org.gdglille.devfest.backend.internals.date.format
import org.gdglille.devfest.backend.third.parties.geocode.GeocodeApi
import org.gdglille.devfest.backend.third.parties.geocode.convertToDb
import org.gdglille.devfest.backend.partners.PartnerDao
import org.gdglille.devfest.backend.partners.Sponsorship
import org.gdglille.devfest.backend.partners.convertToModel
import org.gdglille.devfest.backend.schedulers.ScheduleItemDao
import org.gdglille.devfest.backend.schedulers.convertToModel
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.speakers.convertToModel
import org.gdglille.devfest.backend.talks.TalkDao
import org.gdglille.devfest.backend.talks.convertToModel
import org.gdglille.devfest.models.Agenda
import org.gdglille.devfest.models.CreatedEvent
import org.gdglille.devfest.models.EventList
import org.gdglille.devfest.models.EventPartners
import org.gdglille.devfest.models.EventV2
import org.gdglille.devfest.models.inputs.CategoryInput
import org.gdglille.devfest.models.inputs.CoCInput
import org.gdglille.devfest.models.inputs.CreatingEventInput
import org.gdglille.devfest.models.inputs.EventInput
import org.gdglille.devfest.models.inputs.FeaturesActivatedInput
import org.gdglille.devfest.models.inputs.LunchMenuInput
import java.time.LocalDateTime
import org.gdglille.devfest.backend.qanda.QAndADao
import org.gdglille.devfest.models.Event
import org.gdglille.devfest.models.EventV3

@Suppress("LongParameterList")
class EventRepository(
    private val geocodeApi: GeocodeApi,
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val qAndADao: QAndADao,
    private val talkDao: TalkDao,
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

    suspend fun getV2(eventId: String): EventV2 = coroutineScope {
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val hasPartners = async { partnerDao.hasPartners(eventId) }
        val qanda = async { qAndADao.getAll(eventId, event.defaultLanguage) }
        return@coroutineScope event.convertToModelV2(
            hasPartnerList = hasPartners.await(),
            qanda = qanda.await()
        )
    }

    suspend fun getV3(eventId: String): EventV3 = coroutineScope {
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val hasPartners = async { partnerDao.hasPartners(eventId) }
        val qanda = async { qAndADao.hasQAndA(eventId) }
        return@coroutineScope event.convertToModelV3(
            hasPartnerList = hasPartners.await(),
            hasQandA = qanda.await()
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

    suspend fun updateCategories(eventId: String, apiKey: String, categories: List<CategoryInput>) =
        coroutineScope {
            eventDao.updateCategories(eventId, apiKey, categories.map { it.convertToDb() })
            return@coroutineScope eventId
        }

    suspend fun updateFeatures(eventId: String, apiKey: String, features: FeaturesActivatedInput) =
        coroutineScope {
            eventDao.updateFeatures(eventId, apiKey, features.hasNetworking)
            return@coroutineScope eventId
        }

    suspend fun agenda(eventDb: EventDb) = coroutineScope {
        val schedules = scheduleItemDao.getAll(eventDb.slugId).groupBy { it.startTime }.entries.map {
            async {
                val scheduleItems = it.value.map {
                    async {
                        if (it.talkId == null) it.convertToModel(null)
                        else {
                            val talk =
                                talkDao.get(eventDb.slugId, it.talkId) ?: return@async it.convertToModel(
                                    null
                                )
                            it.convertToModel(
                                talk.convertToModel(
                                    speakerDao.getByIds(eventDb.slugId, talk.speakerIds)
                                        .map { it.convertToModel() },
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
}
