package com.paligot.confily.backend.third.parties.cms4partners

import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.internals.helpers.image.TranscoderImage
import com.paligot.confily.backend.jobs.JobDao
import com.paligot.confily.backend.partners.PartnerDao
import com.paligot.confily.backend.partners.PartnerRepository
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.third.parties.geocode.GeocodeApi
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post

fun Routing.registerCms4PartnersRoutes(
    geocodeApi: GeocodeApi,
    eventDao: EventDao,
    partnerDao: PartnerDao,
    jobDao: JobDao,
    imageTranscoder: TranscoderImage
) {
    val repository = PartnerRepository(geocodeApi, eventDao, partnerDao, jobDao, imageTranscoder)

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
