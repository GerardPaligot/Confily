package com.paligot.confily.backend.integrations.infrastructure.api

import com.paligot.confily.backend.integrations.domain.IntegrationProvider
import com.paligot.confily.backend.integrations.domain.IntegrationUsage
import com.paligot.confily.backend.integrations.infrastructure.factory.IntegrationModule
import com.paligot.confily.backend.internals.infrastructure.ktor.http.Identifier
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.json.Json

fun Route.integrationRoutes() {
    val integrationRepository = IntegrationModule.integrationRepository
    val deserializerRegistry = IntegrationModule.deserializer

    route("/integrations") {
        get {
            val eventId = call.parameters["eventId"]!!
            val integrations = integrationRepository.findByEvent(eventId)
            call.respond(status = HttpStatusCode.OK, message = integrations)
        }

        delete("/{integrationId}") {
            val eventId = call.parameters["eventId"]!!
            val integrationId = call.parameters["integrationId"]!!
            integrationRepository.deleteById(eventId, integrationId)
            call.respond(HttpStatusCode.NoContent)
        }

        post("/{provider}/{usage}") {
            val eventId = call.parameters["eventId"]!!
            val provider = IntegrationProvider.valueOf(call.parameters["provider"]!!.uppercase())
            val usage = IntegrationUsage.valueOf(call.parameters["usage"]!!.uppercase())
            val serializer = deserializerRegistry.serializerFor(provider)
            val json = Json { ignoreUnknownKeys = true }
            val input = json.decodeFromString(serializer, call.receiveText())
            val integrationId = integrationRepository.register(eventId, provider, usage, input)
            call.respond(status = HttpStatusCode.Created, message = Identifier(integrationId))
        }
    }
}
