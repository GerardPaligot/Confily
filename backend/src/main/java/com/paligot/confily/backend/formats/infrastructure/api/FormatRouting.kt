package com.paligot.confily.backend.formats.infrastructure.api

import com.paligot.confily.backend.formats.infrastructure.factory.FormatModule.formatAdminRepository
import com.paligot.confily.backend.formats.infrastructure.factory.FormatModule.formatRepository
import com.paligot.confily.backend.internals.infrastructure.ktor.http.Identifier
import com.paligot.confily.backend.internals.infrastructure.ktor.plugins.PlanningUpdatedAtPlugin
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
        call.respond(status = HttpStatusCode.OK, message = repository.list(eventId))
    }
}

fun Route.registerAdminFormatsRoutes() {
    val repository by formatAdminRepository

    route("/formats") {
        this.install(PlanningUpdatedAtPlugin)
        post {
            val eventId = call.parameters["eventId"]!!
            val formatInput = call.receiveValidated<FormatInput>()
            val id = repository.create(eventId, formatInput)
            call.respond(status = HttpStatusCode.Created, message = Identifier(id))
        }
        put("/{id}") {
            val eventId = call.parameters["eventId"]!!
            val formatId = call.parameters["id"]!!
            val formatInput = call.receiveValidated<FormatInput>()
            val id = repository.update(eventId, formatId, formatInput)
            call.respond(status = HttpStatusCode.OK, message = Identifier(id))
        }
    }
}
