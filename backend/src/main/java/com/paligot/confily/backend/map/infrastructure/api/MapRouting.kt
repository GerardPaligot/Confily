package com.paligot.confily.backend.map.infrastructure.api

import com.paligot.confily.backend.internals.infrastructure.ktor.http.asFile
import com.paligot.confily.backend.map.infrastructure.factory.MapModule.mapAdminRepository
import com.paligot.confily.backend.map.infrastructure.factory.MapModule.mapRepository
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.models.inputs.MapInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.registerMapRoutes() {
    val repository = mapRepository

    get("/maps") {
        val eventId = call.parameters["eventId"]!!
        call.respond(repository.list(eventId))
    }
}

fun Route.registerAdminMapRoutes() {
    val repository = mapAdminRepository

    route("/maps") {
        post {
            val eventId = call.parameters["eventId"]!!
            val multipartData = call.receiveMultipart()
            call.respond(
                status = HttpStatusCode.Created,
                message = repository.create(eventId, multipartData.readPart().asFile())
            )
        }

        put("/{mapId}") {
            val eventId = call.parameters["eventId"]!!
            val id = call.parameters["mapId"]!!
            val input = call.receiveValidated<MapInput>()
            call.respond(
                status = HttpStatusCode.OK,
                message = repository.update(eventId, id, input)
            )
        }

        delete("/{mapId}") {
            val eventId = call.parameters["eventId"]!!
            val id = call.parameters["mapId"]!!
            repository.delete(eventId, id)
            call.respond(HttpStatusCode.NoContent)
        }

        put("/{mapId}/plan") {
            val eventId = call.parameters["eventId"]!!
            val id = call.parameters["mapId"]!!
            val filled = call.request.queryParameters["filled"] == "true"
            val multipartData = call.receiveMultipart()
            call.respond(
                status = HttpStatusCode.OK,
                message = repository.updatePlan(
                    eventId = eventId,
                    mapId = id,
                    file = multipartData.readPart().asFile(),
                    filled = filled
                )
            )
        }
    }
}
