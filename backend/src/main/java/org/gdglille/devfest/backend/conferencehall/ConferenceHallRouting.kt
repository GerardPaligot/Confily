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
    eventDao: EventDao,
    speakerDao: SpeakerDao,
    talkDao: TalkDao
) {
    post("conference-hall/{eventId}/import") {
        val apiKey = call.request.headers["api_key"]!!
        val eventId = call.parameters["eventId"]!!
        val api = ConferenceHallApi.Factory.create(apiKey = apiKey, enableNetworkLogs = true)
        val repository = ConferenceHallRepository(api, eventDao, speakerDao, talkDao)
        call.respond(HttpStatusCode.Created, repository.import(eventId, apiKey))
    }
}
