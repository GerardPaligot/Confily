package com.paligot.conferences.backend.conferencehall

import com.paligot.conferences.backend.events.EventDao
import com.paligot.conferences.backend.network.ConferenceHallApi
import com.paligot.conferences.backend.speakers.SpeakerDao
import com.paligot.conferences.backend.talks.TalkDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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