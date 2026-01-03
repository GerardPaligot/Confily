package com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.api

import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.factory.PartnersConnectModule.partnersConnectRepository
import com.paligot.confily.backend.third.parties.partnersconnect.infrastructure.provider.PartnersConnectWebhookPayload
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerAdminPartnersConnectRoutes() {
    val repository = partnersConnectRepository

    route("/partners-connect") {
        post("/webhook") {
            val eventId = call.parameters["eventId"]!!
            val payload = call.receive<PartnersConnectWebhookPayload>()
            val partnerId = repository.webhook(eventId, payload)
            call.respond(status = HttpStatusCode.Created, message = partnerId)
        }
    }
}
