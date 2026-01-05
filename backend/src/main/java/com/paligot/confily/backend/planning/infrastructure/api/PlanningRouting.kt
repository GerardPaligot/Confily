package com.paligot.confily.backend.planning.infrastructure.api

import com.paligot.confily.backend.planning.infrastructure.factory.PlanningFactory.planningRepository
import com.paligot.confily.backend.version
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.acceptItems
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

@Suppress("LongMethod", "MagicNumber")
fun Route.registerPlanningRoutes() {
    val planningRepository = planningRepository

    get("/agenda") {
        val eventId = call.parameters["eventId"]!!
        val acceptItems = call.request.acceptItems()
        if (acceptItems.map { it.value }.contains("text/csv")) {
            call.respond(
                status = HttpStatusCode.OK,
                message = planningRepository.getCsv(eventId)
            )
            return@get
        }
        when (acceptItems.version()) {
            1 -> call.respond(status = HttpStatusCode.OK, message = planningRepository.agenda(eventId))
            2 -> call.respond(status = HttpStatusCode.OK, message = planningRepository.agendaMultiDays(eventId))
            3 -> call.respond(status = HttpStatusCode.OK, message = planningRepository.planning(eventId))
            4 -> call.respond(status = HttpStatusCode.OK, message = planningRepository.planningBySchedules(eventId))
            else -> call.respond(HttpStatusCode.NotImplemented)
        }
    }
}
