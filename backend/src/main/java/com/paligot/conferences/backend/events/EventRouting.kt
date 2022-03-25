package com.paligot.conferences.backend.events

import com.paligot.conferences.backend.partners.PartnerDao
import com.paligot.conferences.backend.receiveValidated
import com.paligot.conferences.backend.schedulers.ScheduleItemDao
import com.paligot.conferences.backend.speakers.SpeakerDao
import com.paligot.conferences.backend.talks.TalkDao
import com.paligot.conferences.models.inputs.EventInput
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerEventRoutes(
    eventDao: EventDao,
    speakerDao: SpeakerDao,
    talkDao: TalkDao,
    scheduleItemDao: ScheduleItemDao,
    partnerDao: PartnerDao
) {
    val repository = EventRepository(eventDao, speakerDao, talkDao, scheduleItemDao, partnerDao)

    get {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
    put {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val input = call.receiveValidated<EventInput>()
        call.respond(HttpStatusCode.OK, repository.update(eventId, apiKey, input))
    }
    get("agenda") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.agenda(eventId))
    }
}