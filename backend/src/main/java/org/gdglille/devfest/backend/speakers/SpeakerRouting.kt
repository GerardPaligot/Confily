package org.gdglille.devfest.backend.speakers

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.receiveValidated
import org.gdglille.devfest.models.inputs.SpeakerInput

fun Route.registerSpeakersRoutes(
    eventDao: EventDao,
    speakerDao: SpeakerDao
) {
    val repository = SpeakerRepository(eventDao, speakerDao)

    get("/speakers") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
    get("/speakers/{id}") {
        val eventId = call.parameters["eventId"]!!
        val speakerId = call.parameters["id"]!!
        call.respond(HttpStatusCode.OK, repository.get(eventId, speakerId))
    }
    post("/speakers") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val speaker = call.receiveValidated<SpeakerInput>()
        call.respond(HttpStatusCode.Created, repository.create(eventId, apiKey, speaker))
    }
    put("/speakers/{id}") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val speakerId = call.parameters["id"]!!
        val speaker = call.receiveValidated<SpeakerInput>()
        call.respond(HttpStatusCode.OK, repository.update(eventId, apiKey, speakerId, speaker))
    }
}
