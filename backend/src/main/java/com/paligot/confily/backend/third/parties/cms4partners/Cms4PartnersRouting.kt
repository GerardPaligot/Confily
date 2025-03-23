package com.paligot.confily.backend.third.parties.cms4partners

import com.paligot.confily.backend.export.ExportModule.exportPartnersRepository
import com.paligot.confily.backend.partners.PartnerModule.partnerRepository
import com.paligot.confily.backend.receiveValidated
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerAdminCms4PartnersRoutes() {
    val repository by partnerRepository
    val exportPartnersRepository by exportPartnersRepository

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
