package com.paligot.confily.backend.third.parties.billetweb

import com.paligot.confily.backend.events.EventDao
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

fun Routing.registerBilletWebRoutes(
    eventDao: EventDao
) {
    get("billet-web/{barcode}") {
        val eventId = call.parameters["eventId"]!!
        val barcode = call.parameters["barcode"]!!
        val api = BilletWebApi.Factory.create(enableNetworkLogs = true)
        val repository = BilletWebRepository(api, eventDao)
        call.respond(HttpStatusCode.OK, repository.get(eventId, barcode))
    }
}
