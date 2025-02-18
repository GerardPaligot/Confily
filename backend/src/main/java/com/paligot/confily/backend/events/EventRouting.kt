@file:JvmName("EventRoutingKt")

package com.paligot.confily.backend.events

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.events.EventModule.eventDao
import com.paligot.confily.backend.events.EventModule.eventRepository
import com.paligot.confily.backend.events.EventModule.eventRepositoryV2
import com.paligot.confily.backend.events.EventModule.eventRepositoryV3
import com.paligot.confily.backend.events.EventModule.eventRepositoryV4
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.version
import com.paligot.confily.models.inputs.CoCInput
import com.paligot.confily.models.inputs.EventInput
import com.paligot.confily.models.inputs.FeaturesActivatedInput
import com.paligot.confily.models.inputs.LunchMenuInput
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.EntityTagVersion
import io.ktor.http.content.VersionCheckResult
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.acceptItems
import io.ktor.server.request.acceptLanguage
import io.ktor.server.request.receive
import io.ktor.server.response.etag
import io.ktor.server.response.lastModified
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Suppress("LongMethod", "MagicNumber")
fun Routing.registerEventRoutes() {
    val eventDao by eventDao
    val repository by eventRepository
    val repositoryV2 by eventRepositoryV2
    val repositoryV3 by eventRepositoryV3
    val repositoryV4 by eventRepositoryV4

    get("/events") {
        call.respond(HttpStatusCode.OK, repository.list())
    }
    post("/events") {
        val input = call.receiveValidated<com.paligot.confily.models.inputs.CreatingEventInput>()
        val language = call.request.acceptLanguage()
            ?: throw BadRequestException("Accept Language required for this api")
        call.respond(HttpStatusCode.Created, repository.create(input, language))
    }
    get("/events/{eventId}") {
        val eventId = call.parameters["eventId"]!!
        when (call.request.acceptItems().version()) {
            1 -> call.respond(HttpStatusCode.OK, repository.getWithPartners(eventId))
            2 -> call.respond(HttpStatusCode.OK, repositoryV2.getV2(eventId))
            3 -> call.respond(HttpStatusCode.OK, repositoryV3.getV3(eventId))
            4 -> call.respond(HttpStatusCode.OK, repositoryV4.getV4(eventId))
            else -> call.respond(HttpStatusCode.NotImplemented)
        }
    }
    put("/events/{eventId}") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val input = call.receiveValidated<EventInput>()
        call.respond(HttpStatusCode.OK, repository.update(eventId, apiKey, input))
    }
    put("/events/{eventId}/menus") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val input = call.receive<List<LunchMenuInput>>()
        call.respond(HttpStatusCode.OK, repository.updateMenus(eventId, apiKey, input))
    }
    put("/events/{eventId}/coc") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val input = call.receiveValidated<CoCInput>()
        call.respond(HttpStatusCode.OK, repository.updateCoC(eventId, apiKey, input))
    }
    put("/events/{eventId}/features_activated") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val input = call.receiveValidated<FeaturesActivatedInput>()
        call.respond(HttpStatusCode.OK, repository.updateFeatures(eventId, apiKey, input))
    }
    get("/events/{eventId}/planning") {
        val eventId = call.parameters["eventId"]!!
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val zoneId = ZoneId.of("Europe/Paris")
        val lastModified = ZonedDateTime.of(
            LocalDateTime.ofInstant(Instant.ofEpochMilli(event.agendaUpdatedAt), zoneId),
            zoneId
        )
        val etag = lastModified.hashCode().toString()
        val version = EntityTagVersion(etag)
        val result = version.check(call.request.headers)
        if (result == VersionCheckResult.NOT_MODIFIED) {
            call.respond(HttpStatusCode.NotModified)
        } else {
            call.response.lastModified(lastModified)
            call.response.etag(etag)
            call.respond(HttpStatusCode.OK, repository.planning(event))
        }
    }
    get("/events/{eventId}/agenda") {
        val eventId = call.parameters["eventId"]!!
        val event = eventDao.get(eventId) ?: throw NotFoundException("Event $eventId Not Found")
        val zoneId = ZoneId.of("Europe/Paris")
        val lastModified = ZonedDateTime.of(
            LocalDateTime.ofInstant(Instant.ofEpochMilli(event.agendaUpdatedAt), zoneId),
            zoneId
        )
        val etag = lastModified.hashCode().toString()
        val version = EntityTagVersion(etag)
        val result = version.check(call.request.headers)
        if (result == VersionCheckResult.NOT_MODIFIED) {
            call.respond(HttpStatusCode.NotModified)
        } else {
            call.response.lastModified(lastModified)
            call.response.etag(etag)
            when (call.request.acceptItems().version()) {
                1 -> call.respond(HttpStatusCode.OK, repository.agenda(event))
                2 -> call.respond(HttpStatusCode.OK, repositoryV2.agenda(event))
                3 -> call.respond(HttpStatusCode.OK, repositoryV3.agenda(event))
                4 -> call.respond(HttpStatusCode.OK, repositoryV4.agenda(event))
                else -> call.respond(HttpStatusCode.NotImplemented)
            }
        }
    }
    put("/events/{eventId}/agenda") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        call.respond(HttpStatusCode.OK, repositoryV4.generateAgenda(eventId, apiKey))
    }
    get("/events/{eventId}/openfeedback") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repositoryV2.openFeedback(eventId))
    }
}
