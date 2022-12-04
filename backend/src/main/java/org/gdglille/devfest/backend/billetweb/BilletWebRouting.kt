package org.gdglille.devfest.backend.billetweb

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.internals.network.billetweb.BilletWebApi

fun Route.registerBilletWebRoutes(
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
