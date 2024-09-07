package com.paligot.confily.backend.third.parties.billetweb

import com.paligot.confily.backend.third.parties.billetweb.BilletWebModule.billetWebRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

fun Routing.registerBilletWebRoutes() {
    val repository by billetWebRepository

    get("billet-web/{barcode}") {
        val eventId = call.parameters["eventId"]!!
        val barcode = call.parameters["barcode"]!!
        call.respond(HttpStatusCode.OK, repository.get(eventId, barcode))
    }
}
