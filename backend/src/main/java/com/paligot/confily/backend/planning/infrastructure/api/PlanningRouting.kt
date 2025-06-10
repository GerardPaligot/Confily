package com.paligot.confily.backend.planning.infrastructure.api

import com.paligot.confily.backend.internals.infrastructure.factory.FirestoreModule.eventFirestore
import com.paligot.confily.backend.planning.infrastructure.factory.PlanningFactory.planningRepository
import com.paligot.confily.backend.version
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.EntityTagVersion
import io.ktor.http.content.VersionCheckResult
import io.ktor.server.request.acceptItems
import io.ktor.server.response.etag
import io.ktor.server.response.lastModified
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Suppress("LongMethod", "MagicNumber")
fun Route.registerPlanningRoutes() {
    val eventDao by eventFirestore
    val planningRepository by planningRepository

    get("/planning") {
        val eventId = call.parameters["eventId"]!!
        val event = eventDao.get(eventId)
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
            call.respond(HttpStatusCode.OK, planningRepository.agendaMultiDaysAndEventSessions(event))
        }
    }
    get("/agenda") {
        val eventId = call.parameters["eventId"]!!
        val event = eventDao.get(eventId)
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
                1 -> call.respond(HttpStatusCode.OK, planningRepository.agenda(event))
                2 -> call.respond(HttpStatusCode.OK, planningRepository.agendaMultiDays(event))
                3 -> call.respond(HttpStatusCode.OK, planningRepository.planning(event))
                4 -> call.respond(HttpStatusCode.OK, planningRepository.planningBySchedules(event))
                else -> call.respond(HttpStatusCode.NotImplemented)
            }
        }
    }
}
