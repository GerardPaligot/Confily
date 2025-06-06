package com.paligot.confily.backend.events

import com.paligot.confily.backend.infrastructure.firestore.CategoryFirestore
import com.paligot.confily.backend.infrastructure.firestore.CategoryEntity
import com.paligot.confily.backend.formats.FormatDao
import com.paligot.confily.backend.formats.FormatDb
import com.paligot.confily.backend.internals.date.FormatterPattern
import com.paligot.confily.backend.internals.date.format
import com.paligot.confily.backend.partners.PartnerDao
import com.paligot.confily.backend.qanda.QAndADao
import com.paligot.confily.backend.schedules.ScheduleDb
import com.paligot.confily.backend.schedules.ScheduleItemDao
import com.paligot.confily.backend.schedules.convertToModel
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.sessions.TalkDb
import com.paligot.confily.backend.sessions.convertToModel
import com.paligot.confily.backend.speakers.SpeakerDao
import com.paligot.confily.backend.speakers.SpeakerDb
import com.paligot.confily.models.EventV2
import com.paligot.confily.models.OpenFeedback
import com.paligot.confily.models.SessionOF
import com.paligot.confily.models.SocialOF
import com.paligot.confily.models.SpeakerOF
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime

@Suppress("LongParameterList")
class EventRepositoryV2(
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val sessionDao: SessionDao,
    private val categoryDao: CategoryFirestore,
    private val formatDao: FormatDao,
    private val scheduleItemDao: ScheduleItemDao,
    private val partnerDao: PartnerDao,
    private val qAndADao: QAndADao
) {
    suspend fun getV2(eventId: String): EventV2 = coroutineScope {
        val event = eventDao.get(eventId)
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
        categories: List<CategoryEntity>,
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
        categories: List<CategoryEntity>,
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
        val event = eventDao.get(eventId)
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
                SpeakerOF(
                    id = it.id,
                    name = it.displayName,
                    photoUrl = it.photoUrl,
                    socials = it.socials.map {
                        SocialOF(name = it.type.name.lowercase(), link = it.url)
                    }
                )
            }
        }
        return@coroutineScope OpenFeedback(
            sessions = asyncSessions.await().associateBy { it.id },
            speakers = asyncSpeakers.await().associateBy { it.id }
        )
    }
}
