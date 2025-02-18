package com.paligot.confily.backend.formats

import com.paligot.confily.backend.formats.FormatModule.formatRepository
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.models.inputs.FormatInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Route.registerFormatsRoutes() {
    val repository by formatRepository

    get("/formats") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
    get("/formats/{id}") {
        val eventId = call.parameters["eventId"]!!
        val formatId = call.parameters["id"]!!
        call.respond(HttpStatusCode.OK, repository.get(eventId, formatId))
    }
    post("/formats") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val formatInput = call.receiveValidated<FormatInput>()
        call.respond(HttpStatusCode.Created, repository.create(eventId, apiKey, formatInput))
    }
    put("/formats/{id}") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val formatId = call.parameters["id"]!!
        val formatInput = call.receiveValidated<FormatInput>()
        call.respond(HttpStatusCode.OK, repository.update(eventId, apiKey, formatId, formatInput))
    }
}
