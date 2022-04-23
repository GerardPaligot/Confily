package org.gdglille.devfest.backend.events

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
import org.gdglille.devfest.models.OpenFeedback
import org.gdglille.devfest.models.SessionOF
import org.gdglille.devfest.models.SocialOF
import org.gdglille.devfest.models.SpeakerOF
import org.gdglille.devfest.models.inputs.EventInput
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime

class EventRepository(
    private val eventDao: EventDao,
    private val speakerDao: SpeakerDao,
    private val talkDao: TalkDao,
    private val scheduleItemDao: ScheduleItemDao,
    private val partnerDao: PartnerDao
) {
    suspend fun list(eventId: String) = coroutineScope {
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        return@coroutineScope event.convertToModel(
            golds = partnerDao.listValidated(event.year, Sponsorship.Gold),
            silvers = partnerDao.listValidated(event.year, Sponsorship.Silver),
            bronzes = partnerDao.listValidated(event.year, Sponsorship.Bronze),
            others = emptyList()
        )
    }

    suspend fun update(eventId: String, apiKey: String, eventInput: EventInput) = coroutineScope {
        val event = eventDao.getVerified(eventId, apiKey)
        eventDao.createOrUpdate(eventInput.convertToDb(event, eventInput.openFeedbackId, apiKey))
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
                            val talk = talkDao.get(eventId, it.talkId) ?: return@async it.convertToModel(null)
                            it.convertToModel(
                                talk.convertToModel(speakerDao.getByIds(eventId, *talk.speakerIds.toTypedArray()), eventDb)
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

    suspend fun openFeedback(eventId: String) = coroutineScope {
        val agenda = agenda(eventId)
        val talks = agenda.talks.values.flatten().filter { it.talk != null }
        val asyncSessions = async {
            talks.map {
                SessionOF(
                    id = it.id,
                    title = it.talk!!.title,
                    trackTitle = it.room,
                    speakers = it.talk!!.speakers.map { speaker -> speaker.id },
                    startTime = "${LocalDateTime.parse(it.startTime).format(FormatterPattern.SimplifiedIso)}+02:00",
                    endTime = "${LocalDateTime.parse(it.endTime).format(FormatterPattern.SimplifiedIso)}+02:00",
                    tags = arrayListOf(it.talk!!.category)
                )
            }
        }
        val asyncSpeakers = async {
            talks.map { it.talk!!.speakers }.flatten().map {
                val socials = arrayListOf<SocialOF>()
                it.github?.let { github -> socials.add(SocialOF(name = "github", link = github)) }
                it.twitter?.let { twitter -> socials.add(SocialOF(name = "twitter", link = twitter)) }
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