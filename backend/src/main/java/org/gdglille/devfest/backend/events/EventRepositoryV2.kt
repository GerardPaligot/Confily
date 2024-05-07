package org.gdglille.devfest.backend.events

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.categories.CategoryDao
import org.gdglille.devfest.backend.categories.CategoryDb
import org.gdglille.devfest.backend.formats.FormatDao
import org.gdglille.devfest.backend.formats.FormatDb
import org.gdglille.devfest.backend.internals.date.FormatterPattern
import org.gdglille.devfest.backend.internals.date.format
import org.gdglille.devfest.backend.partners.PartnerDao
import org.gdglille.devfest.backend.qanda.QAndADao
import org.gdglille.devfest.backend.schedulers.ScheduleDb
import org.gdglille.devfest.backend.schedulers.ScheduleItemDao
import org.gdglille.devfest.backend.schedulers.convertToModel
import org.gdglille.devfest.backend.sessions.SessionDao
import org.gdglille.devfest.backend.sessions.TalkDb
import org.gdglille.devfest.backend.sessions.convertToModel
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.speakers.SpeakerDb
import org.gdglille.devfest.models.EventV2
import org.gdglille.devfest.models.OpenFeedback
import org.gdglille.devfest.models.SessionOF
import org.gdglille.devfest.models.SocialOF
import org.gdglille.devfest.models.SpeakerOF
import java.time.LocalDateTime

@Suppress("LongParameterList")
class EventRepositoryV2(
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val sessionDao: SessionDao,
    private val categoryDao: CategoryDao,
    private val formatDao: FormatDao,
    private val scheduleItemDao: ScheduleItemDao,
    private val partnerDao: PartnerDao,
    private val qAndADao: QAndADao
) {
    suspend fun getV2(eventId: String): EventV2 = coroutineScope {
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val hasPartners = async { partnerDao.hasPartners(eventId) }
        val qanda = async { qAndADao.getAll(eventId, event.defaultLanguage) }
        return@coroutineScope event.convertToModelV2(
            hasPartnerList = hasPartners.await(),
            qanda = qanda.await()
        )
    }

    suspend fun agenda(eventDb: EventDb) = coroutineScope {
        val talks = sessionDao.getAllTalkSessions(eventDb.slugId)
        val speakers = speakerDao.getAll(eventDb.slugId)
        val categories = categoryDao.getAll(eventDb.slugId)
        val formats = formatDao.getAll(eventDb.slugId)
        return@coroutineScope scheduleItemDao.getAll(eventDb.slugId)
            .groupBy { LocalDateTime.parse(it.startTime).format(FormatterPattern.YearMonthDay) }
            .entries.map { schedulesByDay ->
                schedulesByDay(schedulesByDay, talks, speakers, categories, formats, eventDb)
            }
            .awaitAll()
            .sortedBy { it.first }
            .associate { it }
            .toMap()
    }

    @Suppress("LongParameterList")
    private fun CoroutineScope.schedulesByDay(
        schedulesByDay: Map.Entry<String, List<ScheduleDb>>,
        talks: List<TalkDb>,
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
                        schedulesBySlot(schedule, talks, speakers, categories, formats, eventDb)
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
        talks: List<TalkDb>,
        speakers: List<SpeakerDb>,
        categories: List<CategoryDb>,
        formats: List<FormatDb>,
        eventDb: EventDb
    ) = async {
        if (schedule.talkId == null) {
            schedule.convertToModel(null)
        } else {
            val talk = talks.find { it.id == schedule.talkId }
                ?: return@async schedule.convertToModel(null)
            val speakersTalk = speakers.filter { talk.speakerIds.contains(it.id) }
            val category = categories.find { it.id == talk.category }
            val format = formats.find { it.id == talk.format }
            schedule.convertToModel(
                talk.convertToModel(speakersTalk, category, format, eventDb)
            )
        }
    }

    suspend fun openFeedback(eventId: String) = coroutineScope {
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val agenda = agenda(event)
        val talks = agenda.values.map { it.values.flatten() }.flatten().filter { it.talk != null }
        val asyncSessions = async {
            talks.map {
                SessionOF(
                    id = it.id,
                    title = it.talk!!.title,
                    trackTitle = it.room,
                    speakers = it.talk!!.speakers.map { speaker -> speaker.id },
                    startTime = "${
                    LocalDateTime.parse(it.startTime).format(FormatterPattern.SimplifiedIso)
                    }+02:00",
                    endTime = "${
                    LocalDateTime.parse(it.endTime).format(FormatterPattern.SimplifiedIso)
                    }+02:00",
                    tags = arrayListOf(it.talk!!.category)
                )
            }
        }
        val asyncSpeakers = async {
            talks.map { it.talk!!.speakers }.flatten().map {
                val socials = arrayListOf<SocialOF>()
                it.github?.let { github -> socials.add(SocialOF(name = "github", link = github)) }
                it.twitter?.let { twitter ->
                    socials.add(
                        SocialOF(
                            name = "twitter",
                            link = twitter
                        )
                    )
                }
                SpeakerOF(
                    id = it.id,
                    name = it.displayName,
                    photoUrl = it.photoUrl,
                    socials = socials
                )
            }
        }
        return@coroutineScope OpenFeedback(
            sessions = asyncSessions.await().associateBy { it.id },
            speakers = asyncSpeakers.await().associateBy { it.id }
        )
    }
}
