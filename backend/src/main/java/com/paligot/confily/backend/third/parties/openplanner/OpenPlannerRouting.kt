package com.paligot.confily.backend.third.parties.openplanner

import com.paligot.confily.backend.NotAuthorized
import com.paligot.confily.backend.categories.CategoryDao
import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.internals.CommonApi
import com.paligot.confily.backend.schedules.ScheduleItemDao
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.speakers.SpeakerDao
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post

@Suppress("LongParameterList")
fun Routing.registerOpenPlannerRoutes(
    openPlannerApi: OpenPlannerApi,
    commonApi: CommonApi,
    eventDao: EventDao,
    speakerDao: SpeakerDao,
    sessionDao: SessionDao,
    categoryDao: CategoryDao,
    formatDao: com.paligot.confily.backend.formats.FormatDao,
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
