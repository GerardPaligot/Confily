@file:JvmName("EventRoutingKt")

package com.paligot.confily.backend.events

import com.paligot.confily.backend.NotFoundException
import com.paligot.confily.backend.categories.CategoryDao
import com.paligot.confily.backend.partners.PartnerDao
import com.paligot.confily.backend.qanda.QAndADao
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.schedules.ScheduleItemDao
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.speakers.SpeakerDao
import com.paligot.confily.backend.third.parties.geocode.GeocodeApi
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

@Suppress("LongMethod", "LongParameterList", "MagicNumber")
fun Routing.registerEventRoutes(
    geocodeApi: GeocodeApi,
    eventDao: EventDao,
    speakerDao: SpeakerDao,
    qAndADao: QAndADao,
    sessionDao: SessionDao,
    categoryDao: CategoryDao,
    formatDao: com.paligot.confily.backend.formats.FormatDao,
    scheduleItemDao: ScheduleItemDao,
    partnerDao: PartnerDao
) {
    val repository = EventRepository(
        geocodeApi,
        eventDao,
        speakerDao,
        qAndADao,
        sessionDao,
        categoryDao,
        formatDao,
        scheduleItemDao,
        partnerDao
    )
    val repositoryV2 = EventRepositoryV2(
        eventDao,
        speakerDao,
        sessionDao,
        categoryDao,
        formatDao,
        scheduleItemDao,
        partnerDao,
        qAndADao
    )
    val repositoryV3 = EventRepositoryV3(
        eventDao,
        speakerDao,
        sessionDao,
        categoryDao,
        formatDao,
        scheduleItemDao,
        partnerDao,
        qAndADao
    )
    val repositoryV4 = EventRepositoryV4(
        speakerDao,
        sessionDao,
        categoryDao,
        formatDao,
        scheduleItemDao
    )

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
    get("/events/{eventId}/openfeedback") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repositoryV2.openFeedback(eventId))
    }
}