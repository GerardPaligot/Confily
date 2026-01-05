package com.paligot.confily.backend.categories.infrastructure.api

import com.paligot.confily.backend.categories.infrastructure.factory.CategoryModule.categoryAdminRepository
import com.paligot.confily.backend.categories.infrastructure.factory.CategoryModule.categoryRepository
import com.paligot.confily.backend.internals.infrastructure.ktor.http.Identifier
import com.paligot.confily.backend.receiveValidated
import com.paligot.confily.models.inputs.CategoryInput
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.registerCategoriesRoutes() {
    val repository = categoryRepository

    get("/categories") {
        val eventId = call.parameters["eventId"]!!
        call.respond(status = HttpStatusCode.OK, message = repository.list(eventId))
    }
}

fun Route.registerAdminCategoriesRoutes() {
    val repository = categoryAdminRepository

    route("/categories") {
        post {
            val eventId = call.parameters["eventId"]!!
            val catInput = call.receiveValidated<CategoryInput>()
            val id = repository.create(eventId, catInput)
            call.respond(status = HttpStatusCode.Created, message = Identifier(id))
        }
        put("/{id}") {
            val eventId = call.parameters["eventId"]!!
            val catId = call.parameters["id"]!!
            val catInput = call.receiveValidated<CategoryInput>()
            val id = repository.update(eventId, catId, catInput)
            call.respond(status = HttpStatusCode.OK, message = Identifier(id))
        }
    }
}
