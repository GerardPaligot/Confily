package org.gdglille.devfest.backend.third.parties.conferencehall

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import org.gdglille.devfest.backend.categories.CategoryDao
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.formats.FormatDao
import org.gdglille.devfest.backend.internals.CommonApi
import org.gdglille.devfest.backend.receiveValidated
import org.gdglille.devfest.backend.sessions.SessionDao
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.models.inputs.third.parties.conferencehall.ImportTalkInput

@Suppress("LongParameterList")
fun Routing.registerConferenceHallRoutes(
    conferenceHallApi: ConferenceHallApi,
    commonApi: CommonApi,
    eventDao: EventDao,
    speakerDao: SpeakerDao,
    sessionDao: SessionDao,
    categoryDao: CategoryDao,
    formatDao: FormatDao
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
