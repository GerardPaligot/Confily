package com.paligot.confily.backend.third.parties.welovedevs

import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.jobs.JobDao
import com.paligot.confily.backend.jobs.JobRepository
import com.paligot.confily.backend.partners.PartnerDao
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post

fun Routing.registerWLDRoutes(
    wldApi: WeLoveDevsApi,
    eventDao: EventDao,
    partnerDao: PartnerDao,
    jobDao: JobDao
) {
    val repository = JobRepository(wldApi, eventDao, partnerDao, jobDao)

    post("jobs/wld/import") {
        val apiKey = call.request.headers["api_key"]!!
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.Created, repository.importWld(eventId, apiKey))
    }
}
