package com.paligot.confily.backend.categories

import com.paligot.confily.backend.categories.CategoryModule.categoryRepository
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.models.inputs.CategoryInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put

fun Route.registerCategoriesRoutes() {
    val repository by categoryRepository

    get("/categories") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
}

fun Route.registerAdminCategoriesRoutes() {
    val repository by categoryRepository

    get("/categories") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
    post("/categories") {
        val eventId = call.parameters["eventId"]!!
        val catInput = call.receiveValidated<CategoryInput>()
        call.respond(HttpStatusCode.Created, repository.create(eventId, catInput))
    }
    put("/categories/{id}") {
        val eventId = call.parameters["eventId"]!!
        val catId = call.parameters["id"]!!
        val catInput = call.receiveValidated<CategoryInput>()
        call.respond(HttpStatusCode.OK, repository.update(eventId, catId, catInput))
    }
}
