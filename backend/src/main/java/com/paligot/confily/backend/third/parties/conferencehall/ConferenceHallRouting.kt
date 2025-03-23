package com.paligot.confily.backend.third.parties.conferencehall

import com.paligot.confily.backend.internals.plugins.PlanningUpdatedAtPlugin
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.third.parties.conferencehall.ConferenceHallModule.conferenceHallRepository
import com.paligot.confily.models.inputs.conferencehall.ImportTalkInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerAdminConferenceHallRoutes() {
    val conferenceHallRepo by conferenceHallRepository

    route("/conference-hall") {
        this.install(PlanningUpdatedAtPlugin)
        post("/{eventId}/talks/import") {
            val eventId = call.parameters["eventId"]!!
            val input = call.receiveValidated<ImportTalkInput>()
            call.respond(HttpStatusCode.Created, conferenceHallRepo.importTalks(eventId, input))
        }

        post("/{eventId}/talks/{talkId}/import") {
            val eventId = call.parameters["eventId"]!!
            val talkId = call.parameters["talkId"]!!
            val input = call.receiveValidated<ImportTalkInput>()
            call.respond(
                HttpStatusCode.Created,
                conferenceHallRepo.importTalk(eventId, talkId, input)
            )
        }

        post("/{eventId}/speakers/import") {
            val eventId = call.parameters["eventId"]!!
            call.respond(HttpStatusCode.Created, conferenceHallRepo.importSpeakers(eventId))
        }

        post("/{eventId}/speakers/{speakerId}/import") {
            val eventId = call.parameters["eventId"]!!
            val speakerId = call.parameters["speakerId"]!!
            call.respond(
                HttpStatusCode.Created,
                conferenceHallRepo.importSpeaker(eventId, speakerId)
            )
        }
    }
}
