package com.paligot.confily.backend.tags

import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.backend.tags.TagModule.tagRepository
import com.paligot.confily.models.inputs.TagInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Route.registerTagsRoutes() {
    val repository by tagRepository

    get("/tags") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
}

fun Route.registerAdminTagsRoutes() {
    val repository by tagRepository

    post("/tags") {
        val eventId = call.parameters["eventId"]!!
        val input = call.receiveValidated<TagInput>()
        call.respond(HttpStatusCode.Created, repository.create(eventId, input))
    }
    put("/tags/{id}") {
        val eventId = call.parameters["eventId"]!!
        val catId = call.parameters["id"]!!
        val input = call.receiveValidated<TagInput>()
        call.respond(HttpStatusCode.OK, repository.update(eventId, catId, input))
    }
}
