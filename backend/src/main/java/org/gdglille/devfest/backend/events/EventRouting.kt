@file:JvmName("EventRoutingKt")

package org.gdglille.devfest.backend.events

import io.ktor.http.HttpStatusCode
import io.ktor.http.content.EntityTagVersion
import io.ktor.http.content.VersionCheckResult
import io.ktor.server.application.call
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.acceptItems
import io.ktor.server.request.acceptLanguage
import io.ktor.server.request.receive
import io.ktor.server.response.etag
import io.ktor.server.response.lastModified
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.gdglille.devfest.backend.NotFoundException
import org.gdglille.devfest.backend.categories.CategoryDao
import org.gdglille.devfest.backend.events.v2.EventRepositoryV2
import org.gdglille.devfest.backend.formats.FormatDao
import org.gdglille.devfest.backend.partners.PartnerDao
import org.gdglille.devfest.backend.qanda.QAndADao
import org.gdglille.devfest.backend.receiveValidated
import org.gdglille.devfest.backend.schedulers.ScheduleItemDao
import org.gdglille.devfest.backend.speakers.SpeakerDao
import org.gdglille.devfest.backend.talks.TalkDao
import org.gdglille.devfest.backend.third.parties.geocode.GeocodeApi
import org.gdglille.devfest.backend.version
import org.gdglille.devfest.models.inputs.CoCInput
import org.gdglille.devfest.models.inputs.CreatingEventInput
import org.gdglille.devfest.models.inputs.EventInput
import org.gdglille.devfest.models.inputs.FeaturesActivatedInput
import org.gdglille.devfest.models.inputs.LunchMenuInput
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Suppress("LongParameterList", "MagicNumber")
fun Route.registerEventRoutes(
    geocodeApi: GeocodeApi,
    eventDao: EventDao,
    speakerDao: SpeakerDao,
    qAndADao: QAndADao,
    talkDao: TalkDao,
    categoryDao: CategoryDao,
    formatDao: FormatDao,
    scheduleItemDao: ScheduleItemDao,
    partnerDao: PartnerDao
) {
    val repository = EventRepository(
        geocodeApi,
        eventDao,
        speakerDao,
        qAndADao,
        talkDao,
        categoryDao,
        formatDao,
        scheduleItemDao,
        partnerDao
    )
    val repositoryV2 = EventRepositoryV2(
        eventDao,
        speakerDao,
        talkDao,
        categoryDao,
        formatDao,
        scheduleItemDao
    )

    get("/events") {
        call.respond(HttpStatusCode.OK, repository.list())
    }
    post("/events") {
        val input = call.receiveValidated<CreatingEventInput>()
        val language = call.request.acceptLanguage()
            ?: throw BadRequestException("Accept Language required for this api")
        call.respond(HttpStatusCode.Created, repository.create(input, language))
    }
    get("/events/{eventId}") {
        val eventId = call.parameters["eventId"]!!
        when (call.request.acceptItems().version()) {
            1 -> call.respond(HttpStatusCode.OK, repository.getWithPartners(eventId))
            2 -> call.respond(HttpStatusCode.OK, repository.getV2(eventId))
            3 -> call.respond(HttpStatusCode.OK, repository.getV3(eventId))
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
                else -> call.respond(HttpStatusCode.NotImplemented)
            }
        }
    }
    get("/events/{eventId}/openfeedback") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repositoryV2.openFeedback(eventId))
    }
}
