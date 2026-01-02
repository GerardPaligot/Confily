package com.paligot.confily.backend.export.infrastructure.api

import com.paligot.confily.backend.export.infrastructure.factory.ExportModule.exportEventAdminRepository
import com.paligot.confily.backend.export.infrastructure.factory.ExportModule.exportEventRepository
import com.paligot.confily.backend.export.infrastructure.factory.ExportModule.exportPartnersAdminRepository
import com.paligot.confily.backend.export.infrastructure.factory.ExportModule.exportPartnersRepository
import com.paligot.confily.backend.export.infrastructure.factory.ExportModule.exportPlanningAdminRepository
import com.paligot.confily.backend.export.infrastructure.factory.ExportModule.exportPlanningRepository
import io.ktor.http.ContentType.Text
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put

fun Route.registerExportRoutes() {
    val exportEventRepository by exportEventRepository
    val exportPlanningRepository by exportPlanningRepository
    val exportPartnersRepository by exportPartnersRepository

    get("/export/event") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, exportEventRepository.get(eventId))
    }
    get("/export/planning") {
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
    get("/export/partners") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, exportPartnersRepository.get(eventId))
    }
}

fun Route.registerAdminExportRoutes() {
    val exportEventRepository by exportEventAdminRepository
    val exportPlanningRepository by exportPlanningAdminRepository
    val exportPartnersRepository by exportPartnersAdminRepository

    put("/export/event") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, exportEventRepository.export(eventId))
    }
    put("/export/planning") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, exportPlanningRepository.export(eventId))
    }
    put("/export/partners") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, exportPartnersRepository.export(eventId))
    }
}
