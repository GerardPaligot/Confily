package com.paligot.conferences.backend.speakers

import com.paligot.conferences.backend.NotFoundException
import com.paligot.conferences.backend.events.EventDao
import com.paligot.conferences.backend.receiveValidated
import com.paligot.conferences.models.inputs.SpeakerInput
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerSpeakersRoutes(
    eventDao: EventDao, speakerDao: SpeakerDao
) {
    get("/speakers") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, speakerDao.getAll(eventId).map { it.convertToModel() })
    }
    get("/speakers/{id}") {
        val id = call.parameters["id"]!!
        val eventId = call.parameters["eventId"]!!
        val speaker = speakerDao.get(eventId, id) ?: throw NotFoundException("Speaker with $id is not found")
        call.respond(HttpStatusCode.OK, speaker.convertToModel())
    }
    post("/speakers") {
        val eventId = call.parameters["eventId"]!!
        eventDao.getVerified(eventId, call.request.headers["api_key"])
        val speaker = call.receiveValidated<SpeakerInput>()
        val speakerDb = speaker.convertToDb()
        val id = speakerDao.createOrUpdate(eventId, speakerDb)
        eventDao.updateUpdatedAt(eventId)
        call.respond(HttpStatusCode.Created, id)
    }
    put("/speakers/{id}") {
        val id = call.parameters["id"]!!
        val eventId = call.parameters["eventId"]!!
        eventDao.getVerified(eventId, call.request.headers["api_key"])
        val speaker = call.receiveValidated<SpeakerInput>()
        val speakerDb = speaker.convertToDb(id)
        eventDao.updateUpdatedAt(eventId)
        call.respond(HttpStatusCode.OK, speakerDao.createOrUpdate(eventId, speakerDb))
    }
}
