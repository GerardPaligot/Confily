package com.paligot.confily.backend.activities

import com.paligot.confily.backend.activities.ActivityModule.activityRepository
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.models.inputs.ActivityInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.registerAdminActivitiesRoutes() {
    val repository by activityRepository

    post("/activities") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val activity = call.receiveValidated<ActivityInput>()
        call.respond(HttpStatusCode.Created, repository.create(eventId, apiKey, activity))
    }
}
