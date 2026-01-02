@file:JvmName("EventRoutingKt")

package com.paligot.confily.backend.events.infrastructure.api

import com.paligot.confily.backend.events.infrastructure.factory.EventModule.eventAdminRepository
import com.paligot.confily.backend.events.infrastructure.factory.EventModule.eventRepository
import com.paligot.confily.backend.internals.infrastructure.ktor.http.Identifier
import com.paligot.confily.backend.internals.infrastructure.ktor.plugins.EventUpdatedAtPlugin
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.version
import com.paligot.confily.models.inputs.CoCInput
import com.paligot.confily.models.inputs.CreatingEventInput
import com.paligot.confily.models.inputs.EventInput
import com.paligot.confily.models.inputs.FeaturesActivatedInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.acceptItems
import io.ktor.server.request.acceptLanguage
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

@Suppress("MagicNumber")
fun Route.registerEventRoutes() {
    val eventRepository by eventRepository

    get("/events") {
        call.respond(status = HttpStatusCode.OK, message = eventRepository.list())
    }
    post("/events") {
        val input = call.receiveValidated<CreatingEventInput>()
        val language = call.request.acceptLanguage()
            ?: throw BadRequestException("Accept Language required for this api")
        call.respond(status = HttpStatusCode.Created, message = eventRepository.create(input, language))
    }
    get("/events/{eventId}") {
        val eventId = call.parameters["eventId"]!!
        when (call.request.acceptItems().version()) {
            1 -> call.respond(status = HttpStatusCode.OK, message = eventRepository.getWithPartners(eventId))
            2 -> call.respond(status = HttpStatusCode.OK, message = eventRepository.getV2(eventId))
            3 -> call.respond(status = HttpStatusCode.OK, message = eventRepository.getV3(eventId))
            4 -> call.respond(status = HttpStatusCode.OK, message = eventRepository.getV4(eventId))
            else -> call.respond(HttpStatusCode.NotImplemented)
        }
    }
}

fun Route.registerAdminEventRoutes() {
    val eventAdminRepository by eventAdminRepository

    route("/events/{eventId}") {
        put {
            val eventId = call.parameters["eventId"]!!
            val input = call.receiveValidated<EventInput>()
            val id = eventAdminRepository.update(eventId, input)
            call.respond(status = HttpStatusCode.OK, message = Identifier(id))
        }
        route("/coc") {
            this.install(EventUpdatedAtPlugin)
            put {
                val eventId = call.parameters["eventId"]!!
                val input = call.receiveValidated<CoCInput>()
                val id = eventAdminRepository.updateCoC(eventId, input)
                call.respond(status = HttpStatusCode.OK, message = Identifier(id))
            }
        }
        route("/features_activated") {
            this.install(EventUpdatedAtPlugin)
            put {
                val eventId = call.parameters["eventId"]!!
                val input = call.receiveValidated<FeaturesActivatedInput>()
                val id = eventAdminRepository.updateFeatures(eventId, input)
                call.respond(status = HttpStatusCode.OK, message = Identifier(id))
            }
        }
    }
}
