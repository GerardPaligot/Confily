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

    get("/planning") {
        val eventId = call.parameters["eventId"]!!
        call.respond(status = HttpStatusCode.OK, message = planningRepository.agendaMultiDaysAndEventSessions(eventId))
    }
    get("/agenda") {
        val eventId = call.parameters["eventId"]!!
        when (call.request.acceptItems().version()) {
            1 -> call.respond(status = HttpStatusCode.OK, message = planningRepository.agenda(eventId))
            2 -> call.respond(status = HttpStatusCode.OK, message = planningRepository.agendaMultiDays(eventId))
            3 -> call.respond(status = HttpStatusCode.OK, message = planningRepository.planning(eventId))
            4 -> call.respond(status = HttpStatusCode.OK, message = planningRepository.planningBySchedules(eventId))
            else -> call.respond(HttpStatusCode.NotImplemented)
        }
    }
}
