package com.paligot.confily.backend.partners.infrastructure.api

import com.paligot.confily.backend.internals.infrastructure.ktor.plugins.PartnersUpdatedAtPlugin
import com.paligot.confily.backend.partners.infrastructure.factory.JobModule.jobRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerAdminWLDRoutes() {
    val repository by jobRepository

    route("/jobs") {
        this.install(PartnersUpdatedAtPlugin)
        post("/wld/import") {
            val eventId = call.parameters["eventId"]!!
            call.respond(HttpStatusCode.Created, repository.import(eventId))
        }
    }
}
