package com.paligot.confily.backend.export

import com.paligot.confily.backend.export.ExportModule.exportEventRepository
import com.paligot.confily.backend.export.ExportModule.exportPartnersRepository
import com.paligot.confily.backend.export.ExportModule.exportPlanningRepository
import io.ktor.http.ContentType.Text
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.put

fun Routing.registerExportRoutes() {
    val exportEventRepository by exportEventRepository
    val exportPlanningRepository by exportPlanningRepository
    val exportPartnersRepository by exportPartnersRepository

    get("/events/{eventId}/export/event") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, exportEventRepository.get(eventId))
    }
    get("/events/{eventId}/export/planning") {
        val eventId = call.parameters["eventId"]!!
        val accept = call.request.headers["Accept"] ?: "application/json"
        when {
            accept.contains("text/csv") -> {
                call.respondText(
                    text = exportPlanningRepository.getCsv(eventId),
                    contentType = Text.CSV
                )
            }
            accept.contains("application/json") -> {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = exportPlanningRepository.get(eventId)
                )
            }
            else -> {
                call.respond(
                    status = HttpStatusCode.NotAcceptable,
                    message = "Format not supported. Use application/json or text/csv."
                )
            }
        }
    }
    get("/events/{eventId}/export/partners") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, exportPartnersRepository.get(eventId))
    }
}

fun Routing.registerAdminExportRoutes() {
    val exportEventRepository by exportEventRepository
    val exportPlanningRepository by exportPlanningRepository
    val exportPartnersRepository by exportPartnersRepository

    put("/events/{eventId}/export/event") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, exportEventRepository.export(eventId))
    }
    put("/events/{eventId}/export/planning") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, exportPlanningRepository.export(eventId))
    }
    put("/events/{eventId}/export/partners") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, exportPartnersRepository.export(eventId))
    }
}
