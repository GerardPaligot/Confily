package org.gdglille.devfest.backend.conferencehall

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.internals.network.conferencehall.ConferenceHallApi
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.talks.TalkDao

fun Route.registerConferenceHallRoutes(
    conferenceHallApi: ConferenceHallApi,
    eventDao: EventDao,
    speakerDao: SpeakerDao,
    talkDao: TalkDao
) {
    val conferenceHallRepo =
        ConferenceHallRepository(conferenceHallApi, eventDao, speakerDao, talkDao)

    post("conference-hall/{eventId}/talks/import") {
        val apiKey = call.request.headers["api_key"]!!
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.Created, conferenceHallRepo.importTalks(eventId, apiKey))
    }

    post("conference-hall/{eventId}/talks/{talkId}/import") {
        val apiKey = call.request.headers["api_key"]!!
        val eventId = call.parameters["eventId"]!!
        val talkId = call.parameters["talkId"]!!
        call.respond(HttpStatusCode.Created, conferenceHallRepo.importTalk(eventId, apiKey, talkId))
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
        call.respond(HttpStatusCode.Created, conferenceHallRepo.importSpeaker(eventId, apiKey, speakerId))
    }
}
