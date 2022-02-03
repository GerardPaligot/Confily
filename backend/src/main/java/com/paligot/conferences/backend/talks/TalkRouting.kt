package com.paligot.conferences.backend.talks

import com.paligot.conferences.backend.NotFoundException
import com.paligot.conferences.backend.events.EventDao
import com.paligot.conferences.backend.receiveValidated
import com.paligot.conferences.backend.speakers.SpeakerDao
import com.paligot.conferences.models.inputs.TalkInput
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

fun Route.registerTalksRoutes(
    eventDao: EventDao, speakerDao: SpeakerDao, talkDao: TalkDao
) {
    get("/talks") {
        val eventId = call.parameters["eventId"]!!
        val talks = talkDao.getAll(eventId)
        val asyncItems =
            talks.map { async { it.convertToModel(speakerDao.getByIds(eventId, *it.speakerIds.toTypedArray())) } }
        call.respond(HttpStatusCode.OK, asyncItems.awaitAll())
    }
    get("/talks/{id}") {
        val eventId = call.parameters["eventId"]!!
        val talkId = call.parameters["id"]!!
        val talk = talkDao.get(eventId, talkId) ?: throw NotFoundException("Talk $talkId Not Found")
        eventDao.updateUpdatedAt(eventId)
        call.respond(
            HttpStatusCode.OK, talk.convertToModel(speakerDao.getByIds(eventId, *talk.speakerIds.toTypedArray()))
        )
    }
    post("/talks") {
        val eventId = call.parameters["eventId"]!!
        eventDao.getVerified(eventId, call.request.headers["api_key"])
        val talk = call.receiveValidated<TalkInput>()
        val talkDb = talk.convertToDb()
        val id = talkDao.createOrUpdate(eventId, talkDb)
        eventDao.updateUpdatedAt(eventId)
        call.respond(HttpStatusCode.Created, id)
    }
    put("/talks/{id}") {
        val eventId = call.parameters["eventId"]!!
        eventDao.getVerified(eventId, call.request.headers["api_key"])
        val talkId = call.parameters["id"]!!
        val talk = call.receiveValidated<TalkInput>()
        talkDao.createOrUpdate(eventId, talk.convertToDb(id = talkId))
        eventDao.updateUpdatedAt(eventId)
        call.respond(HttpStatusCode.OK, talkId)
    }
}
