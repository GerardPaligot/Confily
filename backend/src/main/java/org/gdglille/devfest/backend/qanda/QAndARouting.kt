package org.gdglille.devfest.backend.qanda

import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.acceptLanguage
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.receiveValidated
import org.gdglille.devfest.models.inputs.QAndAInput

fun Routing.registerQAndAsRoutes(
    eventDao: EventDao,
    qAndADao: QAndADao
) {
    val repository = QAndARepository(eventDao, qAndADao)

    get("/qanda") {
        val eventId = call.parameters["eventId"]!!
        val language = call.request.acceptLanguage()
            ?: throw BadRequestException("Accept Language required for this api")
        call.respond(HttpStatusCode.OK, repository.list(eventId, language.split("-").first()))
    }
    get("/qanda/{id}") {
        val eventId = call.parameters["eventId"]!!
        val qandaId = call.parameters["id"]!!
        call.respond(HttpStatusCode.OK, repository.get(eventId, qandaId))
    }
    post("/qanda") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val qandaInput = call.receiveValidated<QAndAInput>()
        call.respond(HttpStatusCode.Created, repository.create(eventId, apiKey, qandaInput))
    }
    put("/qanda/{id}") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val qandaId = call.parameters["id"]!!
        val qandaInput = call.receiveValidated<QAndAInput>()
        call.respond(HttpStatusCode.OK, repository.update(eventId, apiKey, qandaId, qandaInput))
    }
}
