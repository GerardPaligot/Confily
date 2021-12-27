package com.paligot.conferences.backend.events

import com.paligot.conferences.backend.schedulers.ScheduleItemDao
import com.paligot.conferences.backend.schedulers.convertToModel
import com.paligot.conferences.backend.speakers.SpeakerDao
import com.paligot.conferences.backend.talks.TalkDao
import com.paligot.conferences.backend.talks.convertToModel
import com.paligot.conferences.models.Agenda
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

fun Route.registerEventRoutes(
    eventDao: EventDao, speakerDao: SpeakerDao, talkDao: TalkDao, scheduleItemDao: ScheduleItemDao
) {
    get {
        val eventId = call.parameters["eventId"]!!
        val event = eventDao.get(eventId)
        if (event == null) {
            call.respond(HttpStatusCode.NotFound, "Event $eventId Not Found")
            return@get
        }
        call.respond(HttpStatusCode.OK, event.convertToModel())
    }
    get("agenda") {
        val eventId = call.parameters["eventId"]!!
        val event = eventDao.get(eventId)
        if (event == null) {
            call.respond(HttpStatusCode.NotFound, "Event $eventId Not Found")
            return@get
        }
        val schedules = scheduleItemDao.getAll(eventId).groupBy { it.time }.entries.map {
            async {
                val scheduleItems = it.value.map {
                    async {
                        if (it.talkId == null) it.convertToModel(null)
                        else {
                            val talk = talkDao.get(eventId, it.talkId) ?: return@async it.convertToModel(null)
                            it.convertToModel(
                                talk.convertToModel(speakerDao.getByIds(eventId, *talk.speakerIds.toTypedArray()))
                            )
                        }
                    }
                }.awaitAll()
                return@async it.key to scheduleItems
            }
        }.awaitAll().associate { it }.toSortedMap()
        call.respond(HttpStatusCode.OK, Agenda(talks = schedules))
    }
}