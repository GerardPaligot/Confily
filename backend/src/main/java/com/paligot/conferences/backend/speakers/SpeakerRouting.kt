package com.paligot.conferences.backend.speakers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerSpeakersRoutes(
  speakerDao: SpeakerDao
) {
  get("/speakers") {
    val eventId = call.parameters["eventId"]!!
    println(eventId)
    call.respond(HttpStatusCode.OK, speakerDao.getAll(eventId).map { it.convertToModel() })
  }
  get("/speakers/{id}") {
    val id = call.parameters["id"]!!
    val eventId = call.parameters["eventId"]!!
    val speaker = speakerDao.get(eventId, id)
    if (speaker == null) {
      call.respond(HttpStatusCode.NotFound, "Speaker with $id is not found")
      return@get
    }
    call.respond(HttpStatusCode.OK, speaker.convertToModel())
  }
  post("/speakers") {
    val eventId = call.parameters["eventId"]!!
    val speaker = call.receive<SpeakerInput>()
    val speakerDb = speaker.convertToDb()
    val id = speakerDao.createOrUpdate(eventId, speakerDb)
    call.respond(HttpStatusCode.Created, id)
  }
  put("/speakers/{id}") {
    val id = call.parameters["id"]!!
    val eventId = call.parameters["eventId"]!!
    val speaker = call.receive<SpeakerInput>()
    val speakerDb = speaker.convertToDb(id)
    call.respond(HttpStatusCode.OK, speakerDao.createOrUpdate(eventId, speakerDb))
  }
}
