package com.paligot.confily.backend.schedules

import com.paligot.confily.backend.internals.infrastructure.ktor.plugins.PlanningUpdatedAtPlugin
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.schedules.ScheduleModule.scheduleRepository
import com.paligot.confily.models.inputs.ScheduleInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerSchedulersRoutes() {
    val repository by scheduleRepository

    get("/schedulers/{id}") {
        val eventId = call.parameters["eventId"]!!
        val scheduleId = call.parameters["id"]!!
        call.respond(HttpStatusCode.OK, repository.get(eventId, scheduleId))
    }
}

fun Route.registerAdminSchedulersRoutes() {
    val repository by scheduleRepository

    route("/schedulers") {
        this.install(PlanningUpdatedAtPlugin)
        post {
            val eventId = call.parameters["eventId"]!!
            val schedule = call.receiveValidated<ScheduleInput>()
            call.respond(HttpStatusCode.Created, repository.create(eventId, schedule))
        }
        delete("/{id}") {
            val eventId = call.parameters["eventId"]!!
            val scheduleId = call.parameters["id"]!!
            repository.delete(eventId, scheduleId)
            call.respond(HttpStatusCode.NoContent, "No Content")
        }
    }
}
