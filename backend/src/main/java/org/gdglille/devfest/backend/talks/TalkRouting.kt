package org.gdglille.devfest.backend.talks

import com.paligot.confily.models.inputs.TalkInput
import com.paligot.confily.models.inputs.TalkVerbatimInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.gdglille.devfest.backend.categories.CategoryDao
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.formats.FormatDao
import org.gdglille.devfest.backend.internals.helpers.drive.GoogleDriveDataSource
import org.gdglille.devfest.backend.receiveValidated
import org.gdglille.devfest.backend.sessions.SessionDao
import org.gdglille.devfest.backend.speakers.SpeakerDao

@Suppress("LongParameterList")
fun Routing.registerTalksRoutes(
    eventDao: EventDao,
    speakerDao: SpeakerDao,
    sessionDao: SessionDao,
    categoryDao: CategoryDao,
    formatDao: FormatDao,
    driveDataSource: GoogleDriveDataSource
) {
    val repository = TalkRepository(
        eventDao,
        speakerDao,
        sessionDao,
        categoryDao,
        formatDao,
        driveDataSource
    )

    get("/talks") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
    get("/talks/{id}") {
        val eventId = call.parameters["eventId"]!!
        val talkId = call.parameters["id"]!!
        call.respond(HttpStatusCode.OK, repository.get(eventId, talkId))
    }
    post("/talks") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val talkInput = call.receiveValidated<TalkInput>()
        call.respond(HttpStatusCode.Created, repository.create(eventId, apiKey, talkInput))
    }
    post("talks/verbatim") {
        val eventId = call.parameters["eventId"]!!
        val verbatim = call.receiveValidated<TalkVerbatimInput>()
        val verbatims = repository.verbatim(eventId, verbatim)
        call.respond(
            status = if (verbatims.isEmpty()) HttpStatusCode.NoContent else HttpStatusCode.Created,
            message = verbatims
        )
    }
    put("/talks/{id}") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val talkId = call.parameters["id"]!!
        val talkInput = call.receiveValidated<TalkInput>()
        call.respond(HttpStatusCode.OK, repository.update(eventId, apiKey, talkId, talkInput))
    }
    post("talks/{id}/verbatim") {
        val eventId = call.parameters["eventId"]!!
        val talkId = call.parameters["id"]!!
        val verbatim = call.receiveValidated<TalkVerbatimInput>()
        call.respond(HttpStatusCode.Created, repository.verbatim(eventId, talkId, verbatim))
    }
}
