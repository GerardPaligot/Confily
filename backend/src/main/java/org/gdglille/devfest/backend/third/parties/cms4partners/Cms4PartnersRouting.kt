package org.gdglille.devfest.backend.third.parties.cms4partners

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.internals.helpers.image.TranscoderImage
import org.gdglille.devfest.backend.jobs.JobDao
import org.gdglille.devfest.backend.partners.PartnerDao
import org.gdglille.devfest.backend.partners.PartnerRepository
import org.gdglille.devfest.backend.receiveValidated
import org.gdglille.devfest.backend.third.parties.geocode.GeocodeApi

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
