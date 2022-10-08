package org.gdglille.devfest.backend.events.v2

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.schedulers.ScheduleItemDao
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.talks.TalkDao

fun Route.registerEventRoutesV2(
    eventDao: EventDao,
    speakerDao: SpeakerDao,
    talkDao: TalkDao,
    scheduleItemDao: ScheduleItemDao
) {
    val repository = EventRepositoryV2(eventDao, speakerDao, talkDao, scheduleItemDao)

    get("agenda") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.agenda(eventId))
    }

    get("openfeedback") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.openFeedback(eventId))
    }
}
