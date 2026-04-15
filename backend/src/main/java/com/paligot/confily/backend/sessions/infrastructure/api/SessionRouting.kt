package com.paligot.confily.backend.sessions.infrastructure.api

import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.sessions.infrastructure.factory.SessionModule.sessionAdminVerbatimRepository
import com.paligot.confily.models.inputs.TalkVerbatimInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerAdminSessionsRoutes() {
    val adminVerbatimRepository = sessionAdminVerbatimRepository

    route("/sessions") {
        post("/verbatim") {
            val eventId = call.parameters["eventId"]!!
            val verbatim = call.receiveValidated<TalkVerbatimInput>()
            val verbatims = adminVerbatimRepository.create(eventId, verbatim)
            call.respond(
                status = if (verbatims.isEmpty()) HttpStatusCode.NoContent else HttpStatusCode.Created,
                message = verbatims
            )
        }
        post("/verbatim/permissions") {
            val eventId = call.parameters["eventId"]!!
            val verbatim = call.receiveValidated<TalkVerbatimInput>()
            val failures = adminVerbatimRepository.grantPermissions(eventId, verbatim)
            call.respond(
                status = HttpStatusCode.Created,
                message = failures
            )
        }
        post("/{id}/verbatim") {
            val eventId = call.parameters["eventId"]!!
            val talkId = call.parameters["id"]!!
            val verbatim = call.receiveValidated<TalkVerbatimInput>()
            call.respond(HttpStatusCode.Created, adminVerbatimRepository.create(eventId, talkId, verbatim))
        }
        post("/{id}/verbatim/permissions") {
            val eventId = call.parameters["eventId"]!!
            val talkId = call.parameters["id"]!!
            val verbatim = call.receiveValidated<TalkVerbatimInput>()
            call.respond(
                status = HttpStatusCode.Created,
                message = adminVerbatimRepository.grantPermissionByTalk(eventId, talkId, verbatim)
            )
        }
    }
}
