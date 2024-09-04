package org.gdglille.devfest.backend.schedules

import com.paligot.confily.models.inputs.ScheduleInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import org.gdglille.devfest.backend.categories.CategoryDao
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.formats.FormatDao
import org.gdglille.devfest.backend.receiveValidated
import org.gdglille.devfest.backend.sessions.SessionDao
import org.gdglille.devfest.backend.speakers.SpeakerDao

@Suppress("LongParameterList")
fun Routing.registerSchedulersRoutes(
    eventDao: EventDao,
    sessionDao: SessionDao,
    categoryDao: CategoryDao,
    formatDao: FormatDao,
    speakerDao: SpeakerDao,
    scheduleItemDao: ScheduleItemDao
) {
    val repository = ScheduleRepository(
        eventDao,
        sessionDao,
        categoryDao,
        formatDao,
        speakerDao,
        scheduleItemDao
    )

    post("/schedulers") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val schedule = call.receiveValidated<ScheduleInput>()
        call.respond(HttpStatusCode.Created, repository.create(eventId, apiKey, schedule))
    }
    get("/schedulers/{id}") {
        val eventId = call.parameters["eventId"]!!
        val scheduleId = call.parameters["id"]!!
        call.respond(HttpStatusCode.OK, repository.get(eventId, scheduleId))
    }
    delete("/schedulers/{id}") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val scheduleId = call.parameters["id"]!!
        repository.delete(eventId, apiKey, scheduleId)
        call.respond(HttpStatusCode.NoContent, "No Content")
    }
}
