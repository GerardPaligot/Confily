package com.paligot.confily.backend.third.parties.conferencehall

import com.paligot.confily.backend.categories.CategoryDao
import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.internals.CommonApi
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.speakers.SpeakerDao
import com.paligot.confily.models.inputs.conferencehall.ImportTalkInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post

@Suppress("LongParameterList")
fun Routing.registerConferenceHallRoutes(
    conferenceHallApi: ConferenceHallApi,
    commonApi: CommonApi,
    eventDao: EventDao,
    speakerDao: SpeakerDao,
    sessionDao: SessionDao,
    categoryDao: CategoryDao,
    formatDao: com.paligot.confily.backend.formats.FormatDao
) {
    val conferenceHallRepo = ConferenceHallRepository(
        conferenceHallApi,
        commonApi,
        eventDao,
        speakerDao,
        sessionDao,
        categoryDao,
        formatDao
    )

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