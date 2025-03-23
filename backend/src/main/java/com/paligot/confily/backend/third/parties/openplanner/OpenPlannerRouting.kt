package com.paligot.confily.backend.third.parties.openplanner

import com.paligot.confily.backend.events.EventModule.eventRepositoryV5
import com.paligot.confily.backend.third.parties.openplanner.OpenPlannerModule.openPlannerRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.registerAdminOpenPlannerRoutes() {
    val repository by openPlannerRepository
    val eventRepository by eventRepositoryV5

    post("openplanner/webhook") {
        val eventId = call.parameters["eventId"]!!
        repository.update(eventId)
        call.respond(HttpStatusCode.Created, eventRepository.generate(eventId))
    }
}
