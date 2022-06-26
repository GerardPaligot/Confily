package org.gdglille.devfest.backend.events

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import org.gdglille.devfest.backend.partners.PartnerDao
import org.gdglille.devfest.backend.receiveValidated
import org.gdglille.devfest.backend.schedulers.ScheduleItemDao
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.talks.TalkDao
import org.gdglille.devfest.models.inputs.EventInput

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
        call.respond(HttpStatusCode.OK, repository.get(eventId))
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
