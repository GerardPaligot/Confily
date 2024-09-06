package com.paligot.confily.backend.schedules

import com.paligot.confily.backend.categories.CategoryDao
import com.paligot.confily.backend.events.EventDao
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.sessions.SessionDao
import com.paligot.confily.backend.speakers.SpeakerDao
import com.paligot.confily.models.inputs.ScheduleInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post

@Suppress("LongParameterList")
fun Routing.registerSchedulersRoutes(
    eventDao: EventDao,
    sessionDao: SessionDao,
    categoryDao: CategoryDao,
    formatDao: com.paligot.confily.backend.formats.FormatDao,
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
