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
import org.gdglille.devfest.backend.internals.CommonApi
import org.gdglille.devfest.backend.schedulers.ScheduleItemDao
import org.gdglille.devfest.backend.sessions.SessionDao
import org.gdglille.devfest.backend.speakers.SpeakerDao

@Suppress("LongParameterList")
fun Route.registerOpenPlannerRoutes(
    openPlannerApi: OpenPlannerApi,
    commonApi: CommonApi,
    eventDao: EventDao,
    speakerDao: SpeakerDao,
    sessionDao: SessionDao,
    categoryDao: CategoryDao,
    formatDao: FormatDao,
    scheduleItemDao: ScheduleItemDao
) {
    val repository = OpenPlannerRepository(
        openPlannerApi,
        commonApi,
        eventDao,
        speakerDao,
        sessionDao,
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
