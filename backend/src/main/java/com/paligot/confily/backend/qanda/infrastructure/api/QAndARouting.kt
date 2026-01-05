package com.paligot.confily.backend.qanda.infrastructure.api

import com.paligot.confily.backend.internals.infrastructure.ktor.http.Identifier
import com.paligot.confily.backend.qanda.infrastructure.factory.QAndAModule.qAndAAdminRepository
import com.paligot.confily.backend.qanda.infrastructure.factory.QAndAModule.qAndARepository
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.models.inputs.QAndAInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.acceptLanguage
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.registerQAndAsRoutes() {
    val repository = qAndARepository

    get("/qanda") {
        val eventId = call.parameters["eventId"]!!
        val language = call.request.acceptLanguage()
            ?: throw BadRequestException("Accept Language required for this api")
        call.respond(status = HttpStatusCode.OK, message = repository.list(eventId, language.split("-").first()))
    }
}

fun Route.registerAdminQAndAsRoutes() {
    val repository = qAndAAdminRepository

    route("/qanda") {
        post {
            val eventId = call.parameters["eventId"]!!
            val qandaInput = call.receiveValidated<QAndAInput>()
            val id = repository.create(eventId, qandaInput)
            call.respond(status = HttpStatusCode.Created, message = Identifier(id))
        }
        put("/{id}") {
            val eventId = call.parameters["eventId"]!!
            val qandaId = call.parameters["id"]!!
            val qandaInput = call.receiveValidated<QAndAInput>()
            val id = repository.update(eventId, qandaId, qandaInput)
            call.respond(status = HttpStatusCode.OK, message = Identifier(id))
        }
    }
}
