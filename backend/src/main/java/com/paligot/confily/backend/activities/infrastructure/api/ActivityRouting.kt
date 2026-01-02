package com.paligot.confily.backend.activities.infrastructure.api

import com.paligot.confily.backend.activities.infrastructure.factory.ActivityModule.activityRepository
import com.paligot.confily.backend.internals.infrastructure.ktor.http.Identifier
import com.paligot.confily.backend.internals.infrastructure.ktor.plugins.PartnersUpdatedAtPlugin
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.models.inputs.ActivityInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.registerAdminActivitiesRoutes() {
    val repository by activityRepository

    route("/partners/{partnerId}/activities") {
        this.install(PartnersUpdatedAtPlugin)
        post {
            val eventId = call.parameters["eventId"]!!
            val partnerId = call.parameters["partnerId"]!!
            val activity = call.receiveValidated<ActivityInput>().copy(partnerId = partnerId)
            val id = repository.create(eventId, activity)
            call.respond(HttpStatusCode.Created, Identifier(id))
        }
    }
}
