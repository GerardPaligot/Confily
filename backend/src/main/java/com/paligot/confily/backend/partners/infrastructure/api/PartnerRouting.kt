package com.paligot.confily.backend.partners.infrastructure.api

import com.paligot.confily.backend.internals.infrastructure.ktor.plugins.PartnersUpdatedAtPlugin
import com.paligot.confily.backend.partners.infrastructure.factory.PartnerModule.partnerAdminRepository
import com.paligot.confily.backend.partners.infrastructure.factory.PartnerModule.partnerRepository
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.models.inputs.PartnerInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.registerPartnersRoutes() {
    val repository by partnerRepository

    get("/partners") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
    get("/partners/activities") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.activities(eventId))
    }
}

fun Route.registerAdminPartnersRoutes() {
    val repository by partnerAdminRepository

    route("/partners") {
        this.install(PartnersUpdatedAtPlugin)
        post {
            val eventId = call.parameters["eventId"]!!
            val partner = call.receiveValidated<PartnerInput>()
            call.respond(HttpStatusCode.Created, repository.create(eventId, partner))
        }
        put("/{id}") {
            val eventId = call.parameters["eventId"]!!
            val partnerId = call.parameters["id"]!!
            val partner = call.receiveValidated<PartnerInput>()
            call.respond(HttpStatusCode.OK, repository.update(eventId, partnerId, partner))
        }
    }
}
