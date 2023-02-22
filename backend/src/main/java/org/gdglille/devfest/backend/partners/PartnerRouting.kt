package org.gdglille.devfest.backend.partners

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.internals.network.geolocation.GeocodeApi
import org.gdglille.devfest.backend.jobs.JobDao
import org.gdglille.devfest.backend.partners.cms4partners.Cms4PartnersDao
import org.gdglille.devfest.backend.receiveValidated
import org.gdglille.devfest.models.inputs.PartnerInput

fun Route.registerPartnersRoutes(
    geocodeApi: GeocodeApi,
    eventDao: EventDao,
    partnerDao: PartnerDao,
    cms4PartnersDao: Cms4PartnersDao,
    jobDao: JobDao
) {
    val repository = PartnerRepository(geocodeApi, eventDao, partnerDao, cms4PartnersDao, jobDao)

    get("/partners") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
    post("/partners") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val partner = call.receiveValidated<PartnerInput>()
        call.respond(HttpStatusCode.Created, repository.create(eventId, apiKey, partner))
    }
    put("/partners/{id}") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val partnerId = call.parameters["id"]!!
        val partner = call.receiveValidated<PartnerInput>()
        call.respond(HttpStatusCode.OK, repository.update(eventId, apiKey, partnerId, partner))
    }
}
