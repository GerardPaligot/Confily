package com.paligot.confily.backend.partners

import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.internals.helpers.image.TranscoderImage
import com.paligot.confily.backend.jobs.JobDao
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.third.parties.geocode.GeocodeApi
import com.paligot.confily.models.inputs.PartnerInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Routing.registerPartnersRoutes(
    geocodeApi: GeocodeApi,
    eventDao: EventDao,
    partnerDao: PartnerDao,
    jobDao: JobDao,
    imageTranscoder: TranscoderImage
) {
    val repository = PartnerRepository(geocodeApi, eventDao, partnerDao, jobDao, imageTranscoder)

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
