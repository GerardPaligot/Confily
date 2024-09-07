package com.paligot.confily.backend.third.parties.conferencehall

import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.third.parties.conferencehall.ConferenceHallModule.conferenceHallRepository
import com.paligot.confily.models.inputs.conferencehall.ImportTalkInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post

fun Routing.registerConferenceHallRoutes() {
    val conferenceHallRepo by conferenceHallRepository

    post("conference-hall/{eventId}/talks/import") {
        val apiKey = call.request.headers["api_key"]!!
        val eventId = call.parameters["eventId"]!!
        val input = call.receiveValidated<ImportTalkInput>()
        call.respond(HttpStatusCode.Created, conferenceHallRepo.importTalks(eventId, apiKey, input))
    }

    post("conference-hall/{eventId}/talks/{talkId}/import") {
        val apiKey = call.request.headers["api_key"]!!
        val eventId = call.parameters["eventId"]!!
        val talkId = call.parameters["talkId"]!!
        val input = call.receiveValidated<ImportTalkInput>()
        call.respond(
            HttpStatusCode.Created,
            conferenceHallRepo.importTalk(eventId, apiKey, talkId, input)
        )
    }

    post("conference-hall/{eventId}/speakers/import") {
        val apiKey = call.request.headers["api_key"]!!
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.Created, conferenceHallRepo.importSpeakers(eventId, apiKey))
    }

    post("conference-hall/{eventId}/speakers/{speakerId}/import") {
        val apiKey = call.request.headers["api_key"]!!
        val eventId = call.parameters["eventId"]!!
        val speakerId = call.parameters["speakerId"]!!
        call.respond(
            HttpStatusCode.Created,
            conferenceHallRepo.importSpeaker(eventId, apiKey, speakerId)
        )
    }
}
