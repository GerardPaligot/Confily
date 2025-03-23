package com.paligot.confily.backend.activities

import com.paligot.confily.backend.activities.ActivityModule.activityRepository
import com.paligot.confily.backend.internals.plugins.PartnersUpdatedAtPlugin
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.models.inputs.ActivityInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerAdminActivitiesRoutes() {
    val repository by activityRepository

    route("/activities") {
        this.install(PartnersUpdatedAtPlugin)

        post("/") {
            val eventId = call.parameters["eventId"]!!
            val activity = call.receiveValidated<ActivityInput>()
            call.respond(HttpStatusCode.Created, repository.create(eventId, activity))
        }
    }
}
