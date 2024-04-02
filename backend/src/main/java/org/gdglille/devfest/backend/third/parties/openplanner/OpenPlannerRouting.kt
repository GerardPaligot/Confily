package org.gdglille.devfest.backend.third.parties.openplanner

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.gdglille.devfest.backend.NotAuthorized
import org.gdglille.devfest.backend.categories.CategoryDao
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.formats.FormatDao
import org.gdglille.devfest.backend.schedulers.ScheduleItemDao
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.talks.TalkDao

@Suppress("LongParameterList")
fun Route.registerOpenPlannerRoutes(
    openPlannerApi: OpenPlannerApi,
    eventDao: EventDao,
    speakerDao: SpeakerDao,
    talkDao: TalkDao,
    categoryDao: CategoryDao,
    formatDao: FormatDao,
    scheduleItemDao: ScheduleItemDao
) {
    val repository = OpenPlannerRepository(
        openPlannerApi,
        eventDao,
        speakerDao,
        talkDao,
        categoryDao,
        formatDao,
        scheduleItemDao
    )

    post("openplanner/webhook") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.queryParameters["api_key"] ?: throw NotAuthorized
        call.respond(HttpStatusCode.Created, repository.update(eventId, apiKey))
    }
}
