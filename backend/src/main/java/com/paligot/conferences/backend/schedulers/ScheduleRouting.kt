package com.paligot.conferences.backend.schedulers

import com.paligot.conferences.backend.speakers.SpeakerDao
import com.paligot.conferences.backend.talks.TalkDao
import com.paligot.conferences.backend.talks.convertToModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerSchedulersRoutes(
    talkDao: TalkDao,
    speakerDao: SpeakerDao,
    scheduleItemDao: ScheduleItemDao
) {
    post("/schedulers") {
        val eventId = call.parameters["eventId"]!!
        val schedule = call.receive<ScheduleInput>()
        if (schedule.talkId == null) {
            val scheduleItem = schedule.convertToDb()
            scheduleItemDao.createOrUpdate(eventId, scheduleItem)
            call.respond(HttpStatusCode.Created, scheduleItem.id)
        } else {
            val talk = talkDao.get(eventId, schedule.talkId)
            if (talk == null) {
                call.respond(HttpStatusCode.NotFound, "Talk ${schedule.talkId} not found")
                return@post
            }
            val scheduleItem = schedule.convertToDb(talk.id)
            scheduleItemDao.createOrUpdate(eventId, scheduleItem)
            call.respond(HttpStatusCode.Created, scheduleItem.id)
        }
    }
    get("/schedulers/{id}") {
        val id = call.parameters["id"]!!
        val eventId = call.parameters["eventId"]!!
        val scheduleItem = scheduleItemDao.get(eventId, id)
        if (scheduleItem == null) {
            call.respond(HttpStatusCode.NotFound, "Schedule item $id not found")
            return@get
        }
        val talk = if (scheduleItem.talkId != null) {
            val talkDb = talkDao.get(eventId, scheduleItem.talkId)
            if (talkDb == null) {
                call.respond(HttpStatusCode.NotFound, "Talk ${scheduleItem.talkId} not found")
                return@get
            }
            val speakers = speakerDao.getByIds(eventId, *talkDb.speakerIds.toTypedArray())
            talkDb.convertToModel(speakers)
        } else null
        call.respond(HttpStatusCode.OK, scheduleItem.convertToModel(talk))
    }
    delete("/schedulers/{id}") {
        val eventId = call.parameters["eventId"]!!
        val id = call.parameters["id"]!!
        scheduleItemDao.delete(eventId, id)
        call.respond(HttpStatusCode.NoContent, "No Content")
    }
}