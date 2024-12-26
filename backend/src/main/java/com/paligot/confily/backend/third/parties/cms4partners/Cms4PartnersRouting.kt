package com.paligot.confily.backend.third.parties.cms4partners

import com.paligot.confily.backend.partners.PartnerModule.partnerRepository
import com.paligot.confily.backend.receiveValidated
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.registerCms4PartnersRoutes() {
    val repository by partnerRepository

    post("cms4partners/webhook") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.parameters["api_key"]!!
        val input = call.receiveValidated<WebhookInput>()
        call.respond(
            HttpStatusCode.Created,
            repository.update(eventId, apiKey, input.id, input.mapToPartnerInput())
        )
    }
}
