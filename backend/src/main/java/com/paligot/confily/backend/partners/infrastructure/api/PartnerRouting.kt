package com.paligot.confily.backend.partners.infrastructure.api

import com.paligot.confily.backend.partners.infrastructure.factory.PartnerModule.partnerRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.registerPartnersRoutes() {
    val repository = partnerRepository

    get("/partners") {
        val eventId = call.parameters["eventId"]!!
        call.respond(status = HttpStatusCode.OK, message = repository.list(eventId))
    }
    get("/partners/activities") {
        val eventId = call.parameters["eventId"]!!
        call.respond(status = HttpStatusCode.OK, message = repository.activities(eventId))
    }
}
