package com.paligot.confily.backend.speakers

import com.paligot.confily.backend.internals.plugins.PlanningUpdatedAtPlugin
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.speakers.SpeakerModule.speakerRepository
import com.paligot.confily.models.inputs.SpeakerInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.registerSpeakersRoutes() {
    val repository by speakerRepository

    get("/speakers") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
}

fun Route.registerAdminSpeakersRoutes() {
    val repository by speakerRepository

    route("/speakers") {
        this.install(PlanningUpdatedAtPlugin)
        post("/") {
            val eventId = call.parameters["eventId"]!!
            val speaker = call.receiveValidated<SpeakerInput>()
            call.respond(HttpStatusCode.Created, repository.create(eventId, speaker))
        }
        put("/{id}") {
            val eventId = call.parameters["eventId"]!!
            val speakerId = call.parameters["id"]!!
            val speaker = call.receiveValidated<SpeakerInput>()
            call.respond(HttpStatusCode.OK, repository.update(eventId, speakerId, speaker))
        }
    }
}
