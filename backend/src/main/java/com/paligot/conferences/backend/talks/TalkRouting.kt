package com.paligot.conferences.backend.talks

import com.paligot.conferences.backend.speakers.SpeakerDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

fun Route.registerTalksRoutes(
    speakerDao: SpeakerDao, talkDao: TalkDao
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
        val talk = talkDao.get(eventId, talkId)
        if (talk == null) {
            call.respond(HttpStatusCode.NotFound, "Talk $talkId Not Found")
            return@get
        }
        call.respond(
            HttpStatusCode.OK, talk.convertToModel(speakerDao.getByIds(eventId, *talk.speakerIds.toTypedArray()))
        )
    }
    post("/talks") {
        val eventId = call.parameters["eventId"]!!
        val talk = call.receive<TalkInput>()
        val talkDb = talk.convertToDb()
        val id = talkDao.createOrUpdate(eventId, talkDb)
        call.respond(HttpStatusCode.Created, id)
    }
    put("/talks/{id}") {
        val eventId = call.parameters["eventId"]!!
        val talkId = call.parameters["id"]!!
        val talk = call.receive<TalkInput>()
        talkDao.createOrUpdate(eventId, talk.convertToDb(id = talkId))
        call.respond(HttpStatusCode.OK, talkId)
    }
}
