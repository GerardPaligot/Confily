package com.paligot.confily.backend.quiz.infrastructure.api

import com.paligot.confily.backend.internals.infrastructure.ktor.http.Identifier
import com.paligot.confily.backend.quiz.infrastructure.factory.QuizModule.quizRepository
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.models.inputs.QuizPlayerInput
import com.paligot.confily.models.inputs.QuizSubmissionInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerQuizRoutes() {
    val repository = quizRepository

    route("/quiz") {
        post("/players") {
            val eventId = call.parameters["eventId"]!!
            val input = call.receiveValidated<QuizPlayerInput>()
            val playerId = repository.registerPlayer(eventId, input)
            call.respond(status = HttpStatusCode.Created, message = Identifier(playerId))
        }
        get("/partners/{code}") {
            val eventId = call.parameters["eventId"]!!
            val code = call.parameters["code"]!!
            call.respond(status = HttpStatusCode.OK, message = repository.questionsByCode(eventId, code))
        }
        post("/partners/{code}/submit") {
            val eventId = call.parameters["eventId"]!!
            val code = call.parameters["code"]!!
            val input = call.receiveValidated<QuizSubmissionInput>()
            call.respond(status = HttpStatusCode.Created, message = repository.submit(eventId, code, input))
        }
        get("/leaderboard") {
            val eventId = call.parameters["eventId"]!!
            call.respond(status = HttpStatusCode.OK, message = repository.leaderboard(eventId))
        }
    }
}
