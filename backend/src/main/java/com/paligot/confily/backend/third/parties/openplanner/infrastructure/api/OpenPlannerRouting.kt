package com.paligot.confily.backend.third.parties.openplanner.infrastructure.api

import com.paligot.confily.backend.internals.infrastructure.ktor.http.Identifier
import com.paligot.confily.backend.third.parties.openplanner.infrastructure.factory.OpenPlannerModule.openPlannerRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerAdminOpenPlannerRoutes() {
    val repository = openPlannerRepository

    route("/openplanner") {
        post("/webhook") {
            val eventId = call.parameters["eventId"]!!
            repository.update(eventId)
            call.respond(HttpStatusCode.Created, Identifier(eventId))
        }
    }
}
