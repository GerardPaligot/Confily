package org.gdglille.devfest.backend.sessions

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.receiveValidated
import org.gdglille.devfest.backend.third.parties.geocode.GeocodeApi
import org.gdglille.devfest.models.inputs.EventSessionInput

fun Route.registerSessionsRoutes(
    geocodeApi: GeocodeApi,
    eventDao: EventDao,
    sessionDao: SessionDao
) {
    val repository = SessionRepository(
        geocodeApi,
        eventDao,
        sessionDao
    )

    get("/sessions") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
    put("/sessions/{sessionId}/event") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val sessionId = call.parameters["sessionId"]!!
        val input = call.receiveValidated<EventSessionInput>()
        call.respond(HttpStatusCode.OK, repository.update(eventId, apiKey, sessionId, input))
    }
}
