package com.paligot.confily.backend.sessions

import com.paligot.confily.backend.internals.plugins.PlanningUpdatedAtPlugin
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.sessions.SessionModule.sessionRepository
import com.paligot.confily.models.inputs.EventSessionInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.registerSessionsRoutes() {
    val repository by sessionRepository

    get("/sessions") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
}

fun Route.registerAdminSessionsRoutes() {
    val repository by sessionRepository

    route("/sessions") {
        this.install(PlanningUpdatedAtPlugin)
        put("/{sessionId}/event") {
            val eventId = call.parameters["eventId"]!!
            val sessionId = call.parameters["sessionId"]!!
            val input = call.receiveValidated<EventSessionInput>()
            call.respond(HttpStatusCode.OK, repository.update(eventId, sessionId, input))
        }
    }
}
