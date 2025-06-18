package com.paligot.confily.backend.third.parties.cms4conference.infrastructure.api

import com.paligot.confily.backend.export.infrastructure.factory.ExportModule.exportPartnersAdminRepository
import com.paligot.confily.backend.partners.infrastructure.factory.PartnerModule.partnerAdminRepository
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.third.parties.cms4conference.application.mapToPartnerInput
import com.paligot.confily.backend.third.parties.cms4conference.infrastructure.provider.WebhookInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerAdminCms4PartnersRoutes() {
    val repository by partnerAdminRepository
    val exportPartnersRepository by exportPartnersAdminRepository

    route("/cms4partners") {
        post("/webhook") {
            val eventId = call.parameters["eventId"]!!
            val input = call.receiveValidated<WebhookInput>()
            val partnerId = repository.update(eventId, input.id, input.mapToPartnerInput())
            exportPartnersRepository.export(eventId)
            call.respond(HttpStatusCode.Created, partnerId)
        }
    }
}
