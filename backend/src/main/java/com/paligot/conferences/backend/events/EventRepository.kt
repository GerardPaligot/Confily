package com.paligot.conferences.backend.events

import com.paligot.conferences.backend.NotFoundException
import com.paligot.conferences.backend.partners.PartnerDao
import com.paligot.conferences.backend.partners.Sponsorship
import com.paligot.conferences.backend.schedulers.ScheduleItemDao
import com.paligot.conferences.backend.schedulers.convertToModel
import com.paligot.conferences.backend.speakers.SpeakerDao
import com.paligot.conferences.backend.talks.TalkDao
import com.paligot.conferences.backend.talks.convertToModel
import com.paligot.conferences.models.Agenda
import com.paligot.conferences.models.OpenFeedback
import com.paligot.conferences.models.SessionOF
import com.paligot.conferences.models.SocialOF
import com.paligot.conferences.models.SpeakerOF
import com.paligot.conferences.models.inputs.EventInput
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
        val schedules = scheduleItemDao.getAll(eventId).groupBy { it.time }.entries.map {
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
                return@async it.key to scheduleItems.sortedBy { it.room }
            }
        }.awaitAll().associate { it }.toSortedMap()
        return@coroutineScope Agenda(talks = schedules)
    }

    suspend fun openFeedback(eventId: String) = coroutineScope {
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val startDate = LocalDateTime.parse(event.startDate.dropLast(1))
        val agenda = agenda(eventId)
        val talks = agenda.talks.values.flatten().filter { it.talk != null }
        val asyncSessions = async {
            talks.map {
                val (hour, minute) = it.time.split(":")
                val startTime = startDate.withHour(hour.toInt()).withMinute(minute.toInt())
                val endTime = when(it.talk!!.format) {
                    "Conférence" -> startTime.plusMinutes(50)
                    "Quickie" -> startTime.plusMinutes(20)
                    else -> TODO("Conference format not implemented")
                }
                SessionOF(
                    id = it.id,
                    title = it.talk!!.title,
                    trackTitle = it.room,
                    speakers = it.talk!!.speakers.map { speaker -> speaker.id },
                    startTime = "$startTime:00+02:00",
                    endTime = "$endTime:00+02:00",
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