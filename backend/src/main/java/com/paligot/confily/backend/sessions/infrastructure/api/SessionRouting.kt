package com.paligot.confily.backend.sessions.infrastructure.api

import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.sessions.infrastructure.factory.SessionModule.sessionAdminRepository
import com.paligot.confily.backend.sessions.infrastructure.factory.SessionModule.sessionAdminVerbatimRepository
import com.paligot.confily.backend.sessions.infrastructure.factory.SessionModule.sessionRepository
import com.paligot.confily.models.inputs.EventSessionInput
import com.paligot.confily.models.inputs.TalkSessionInput
import com.paligot.confily.models.inputs.TalkVerbatimInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.registerSessionsRoutes() {
    val repository = sessionRepository

    get("/sessions") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
}

fun Route.registerAdminSessionsRoutes() {
    val adminRepository = sessionAdminRepository
    val adminVerbatimRepository = sessionAdminVerbatimRepository

    route("/sessions") {
        post {
            val eventId = call.parameters["eventId"]!!
            val input = call.receiveValidated<TalkSessionInput>()
            call.respond(HttpStatusCode.Created, adminRepository.create(eventId, input))
        }
        put("/{id}/talk") {
            val eventId = call.parameters["eventId"]!!
            val talkId = call.parameters["id"]!!
            val input = call.receiveValidated<TalkSessionInput>()
            call.respond(HttpStatusCode.OK, adminRepository.update(eventId, talkId, input))
        }
        put("/{id}/event") {
            val eventId = call.parameters["eventId"]!!
            val sessionId = call.parameters["id"]!!
            val input = call.receiveValidated<EventSessionInput>()
            call.respond(HttpStatusCode.OK, adminRepository.update(eventId, sessionId, input))
        }
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
            adminVerbatimRepository.grantPermissions(eventId, verbatim)
            call.respond(HttpStatusCode.Created)
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
