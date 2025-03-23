package com.paligot.confily.backend.third.parties.welovedevs

import com.paligot.confily.backend.internals.plugins.PartnersUpdatedAtPlugin
import com.paligot.confily.backend.jobs.JobModule.jobRepository
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
            call.respond(HttpStatusCode.Created, repository.importWld(eventId))
        }
    }
}
