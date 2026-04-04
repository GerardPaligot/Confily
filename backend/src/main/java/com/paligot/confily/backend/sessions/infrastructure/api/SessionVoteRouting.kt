package com.paligot.confily.backend.sessions.infrastructure.api

import com.paligot.confily.backend.sessions.infrastructure.factory.SessionModule.sessionVoteRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerSessionVoteRoutes() {
    val voteRepository = sessionVoteRepository

    route("/sessions") {
        post("/{id}/vote") {
            val eventId = call.parameters["eventId"]!!
            val sessionId = call.parameters["id"]!!
            voteRepository.vote(eventId, sessionId)
            call.respond(HttpStatusCode.Created)
        }
    }
}

fun Route.registerAdminSessionVoteRoutes() {
    val voteRepository = sessionVoteRepository

    route("/sessions") {
        get("/votes") {
            val eventId = call.parameters["eventId"]!!
            call.respond(HttpStatusCode.OK, voteRepository.sessionsSortedByVotes(eventId))
        }
    }
}
