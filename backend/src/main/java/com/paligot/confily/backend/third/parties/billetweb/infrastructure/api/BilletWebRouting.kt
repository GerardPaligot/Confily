package com.paligot.confily.backend.third.parties.billetweb.infrastructure.api

import com.paligot.confily.backend.third.parties.billetweb.infrastructure.factory.BilletWebModule.billetWebRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.registerBilletWebRoutes() {
    val repository by billetWebRepository

    get("billet-web/{barcode}") {
        val eventId = call.parameters["eventId"]!!
        val barcode = call.parameters["barcode"]!!
        call.respond(HttpStatusCode.OK, repository.get(eventId, barcode))
    }
}
