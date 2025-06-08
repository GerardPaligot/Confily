package com.paligot.confily.backend.third.parties.openfeedback.infrastructure.api

import com.paligot.confily.backend.third.parties.openfeedback.infrastructure.factory.OpenfeedbackModule.openfeedbackRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlin.getValue

fun Route.registerOpenfeedackRoutes() {
    val openfeedbackRepository by openfeedbackRepository

    get("/openfeedback") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, openfeedbackRepository.get(eventId))
    }
}
