package org.gdglille.devfest.backend.events

import org.gdglille.devfest.backend.partners.PartnerDao
import org.gdglille.devfest.backend.receiveValidated
import org.gdglille.devfest.backend.schedulers.ScheduleItemDao
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.talks.TalkDao
import org.gdglille.devfest.models.inputs.EventInput
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
    get("openfeedback") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.openFeedback(eventId))
    }
}