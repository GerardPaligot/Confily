package com.paligot.confily.backend.third.parties.openplanner

import com.paligot.confily.backend.export.ExportModule.exportEventRepository
import com.paligot.confily.backend.export.ExportModule.exportPlanningRepository
import com.paligot.confily.backend.third.parties.openplanner.OpenPlannerModule.openPlannerRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerAdminOpenPlannerRoutes() {
    val repository by openPlannerRepository
    val exportEventRepository by exportEventRepository
    val exportPlanningRepository by exportPlanningRepository

    route("/openplanner") {
        post("/webhook") {
            val eventId = call.parameters["eventId"]!!
            repository.update(eventId)
            exportPlanningRepository.export(eventId)
            exportEventRepository.export(eventId)
            call.respond(HttpStatusCode.Created, eventId)
        }
    }
}
