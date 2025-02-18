package com.paligot.confily.backend.third.parties.welovedevs

import com.paligot.confily.backend.jobs.JobModule.jobRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.registerWLDRoutes() {
    val repository by jobRepository

    post("jobs/wld/import") {
        val apiKey = call.request.headers["api_key"]!!
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.Created, repository.importWld(eventId, apiKey))
    }
}
