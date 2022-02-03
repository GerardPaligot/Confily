package com.paligot.conferences.backend.schedulers

import com.paligot.conferences.backend.NotFoundException
import com.paligot.conferences.backend.events.EventDao
import com.paligot.conferences.backend.receiveValidated
import com.paligot.conferences.backend.speakers.SpeakerDao
import com.paligot.conferences.backend.talks.TalkDao
import com.paligot.conferences.backend.talks.convertToModel
import com.paligot.conferences.models.inputs.ScheduleInput
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerSchedulersRoutes(
    eventDao: EventDao,
    talkDao: TalkDao,
    speakerDao: SpeakerDao,
    scheduleItemDao: ScheduleItemDao
) {
    post("/schedulers") {
        val eventId = call.parameters["eventId"]!!
        val schedule = call.receiveValidated<ScheduleInput>()
        if (schedule.talkId == null) {
            val scheduleItem = schedule.convertToDb()
            scheduleItemDao.createOrUpdate(eventId, scheduleItem)
            eventDao.updateUpdatedAt(eventId)
            call.respond(HttpStatusCode.Created, scheduleItem.id)
        } else {
            val talk = talkDao.get(eventId, schedule.talkId!!)
                ?: throw NotFoundException("Talk ${schedule.talkId} not found")
            val scheduleItem = schedule.convertToDb(talk.id)
            scheduleItemDao.createOrUpdate(eventId, scheduleItem)
            eventDao.updateUpdatedAt(eventId)
            call.respond(HttpStatusCode.Created, scheduleItem.id)
        }
    }
    get("/schedulers/{id}") {
        val id = call.parameters["id"]!!
        val eventId = call.parameters["eventId"]!!
        val scheduleItem = scheduleItemDao.get(eventId, id) ?: throw NotFoundException("Schedule item $id not found")
        val talk = if (scheduleItem.talkId != null) {
            val talkDb = talkDao.get(eventId, scheduleItem.talkId)
                ?: throw NotFoundException("Talk ${scheduleItem.talkId} not found")
            val speakers = speakerDao.getByIds(eventId, *talkDb.speakerIds.toTypedArray())
            talkDb.convertToModel(speakers)
        } else null
        eventDao.updateUpdatedAt(eventId)
        call.respond(HttpStatusCode.OK, scheduleItem.convertToModel(talk))
    }
    delete("/schedulers/{id}") {
        val eventId = call.parameters["eventId"]!!
        val id = call.parameters["id"]!!
        scheduleItemDao.delete(eventId, id)
        eventDao.updateUpdatedAt(eventId)
        call.respond(HttpStatusCode.NoContent, "No Content")
    }
}