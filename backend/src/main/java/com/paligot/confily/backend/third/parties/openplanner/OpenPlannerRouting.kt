package com.paligot.confily.backend.third.parties.openplanner

import com.paligot.confily.backend.NotAuthorized
import com.paligot.confily.backend.third.parties.openplanner.OpenPlannerModule.openPlannerRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post

fun Routing.registerOpenPlannerRoutes() {
    val repository by openPlannerRepository

    post("openplanner/webhook") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.queryParameters["api_key"] ?: throw NotAuthorized
        call.respond(HttpStatusCode.Created, repository.update(eventId, apiKey))
    }
}
