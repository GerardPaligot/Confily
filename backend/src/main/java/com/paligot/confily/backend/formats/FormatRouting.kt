package com.paligot.confily.backend.formats

import com.paligot.confily.backend.formats.FormatModule.formatRepository
import com.paligot.confily.backend.internals.plugins.PlanningUpdatedAtPlugin
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.models.inputs.FormatInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.registerFormatsRoutes() {
    val repository by formatRepository

    get("/formats") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
}

fun Route.registerAdminFormatsRoutes() {
    val repository by formatRepository

    route("/formats") {
        this.install(PlanningUpdatedAtPlugin)
        post("/") {
            val eventId = call.parameters["eventId"]!!
            val formatInput = call.receiveValidated<FormatInput>()
            call.respond(HttpStatusCode.Created, repository.create(eventId, formatInput))
        }
        put("/{id}") {
            val eventId = call.parameters["eventId"]!!
            val formatId = call.parameters["id"]!!
            val formatInput = call.receiveValidated<FormatInput>()
            call.respond(HttpStatusCode.OK, repository.update(eventId, formatId, formatInput))
        }
    }
}
