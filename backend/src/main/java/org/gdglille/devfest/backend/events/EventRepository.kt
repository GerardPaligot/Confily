package org.gdglille.devfest.backend.events

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.date.FormatterPattern
import org.gdglille.devfest.backend.date.format
import org.gdglille.devfest.backend.partners.PartnerDao
import org.gdglille.devfest.backend.partners.Sponsorship
import org.gdglille.devfest.backend.schedulers.ScheduleItemDao
import org.gdglille.devfest.backend.schedulers.convertToModel
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.talks.TalkDao
import org.gdglille.devfest.backend.talks.convertToModel
import org.gdglille.devfest.models.Agenda
import org.gdglille.devfest.models.inputs.CategoryInput
import org.gdglille.devfest.models.inputs.CoCInput
import org.gdglille.devfest.models.inputs.EventInput
import org.gdglille.devfest.models.inputs.FeaturesActivatedInput
import org.gdglille.devfest.models.inputs.LunchMenuInput
import org.gdglille.devfest.models.inputs.QuestionAndResponseInput
import java.time.LocalDateTime

class EventRepository(
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val talkDao: TalkDao,
    private val scheduleItemDao: ScheduleItemDao,
    private val partnerDao: PartnerDao
) {
    suspend fun get(eventId: String) = coroutineScope {
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val partners = partnerDao.getAll(eventId)
        val golds = partnerDao.listValidatedFromCms4Partners(event.year, Sponsorship.Gold)
            .plus(partners.filter { it.sponsoring == Sponsorship.Gold.name })
        val silvers = partnerDao.listValidatedFromCms4Partners(event.year, Sponsorship.Silver)
            .plus(partners.filter { it.sponsoring == Sponsorship.Silver.name })
        val bronzes = partnerDao.listValidatedFromCms4Partners(event.year, Sponsorship.Bronze)
            .plus(partners.filter { it.sponsoring == Sponsorship.Bronze.name })
        return@coroutineScope event.convertToModel(
            golds = golds.sortedBy { it.name },
            silvers = silvers.sortedBy { it.name },
            bronzes = bronzes.sortedBy { it.name },
            others = partners.filter { it.sponsoring == Sponsorship.Other.name }
                .sortedBy { it.name }
        )
    }

    suspend fun update(eventId: String, apiKey: String, eventInput: EventInput) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        eventDao.createOrUpdate(eventInput.convertToDb(event, eventInput.openFeedbackId, apiKey))
        return@coroutineScope eventId
    }

    suspend fun updateMenus(eventId: String, apiKey: String, menus: List<LunchMenuInput>) =
        coroutineScope {
            eventDao.updateMenus(eventId, apiKey, menus.map { it.convertToDb() })
            return@coroutineScope eventId
        }

    suspend fun updateQAndA(
        eventId: String,
        apiKey: String,
        qAndA: List<QuestionAndResponseInput>
    ) = coroutineScope {
        eventDao.updateQuestionsAndResponses(
            eventId,
            apiKey,
            qAndA.mapIndexed { index, it -> it.convertToDb(index) }
        )
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

    suspend fun agenda(eventId: String) = coroutineScope {
        val eventDb = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val schedules = scheduleItemDao.getAll(eventId).groupBy { it.startTime }.entries.map {
            async {
                val scheduleItems = it.value.map {
                    async {
                        if (it.talkId == null) it.convertToModel(null)
                        else {
                            val talk =
                                talkDao.get(eventId, it.talkId) ?: return@async it.convertToModel(
                                    null
                                )
                            it.convertToModel(
                                talk.convertToModel(
                                    speakerDao.getByIds(eventId, talk.speakerIds),
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
