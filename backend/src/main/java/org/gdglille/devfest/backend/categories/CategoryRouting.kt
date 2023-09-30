package org.gdglille.devfest.backend.categories

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import org.gdglille.devfest.backend.events.EventDao
import org.gdglille.devfest.backend.receiveValidated
import org.gdglille.devfest.models.inputs.CategoryInput

fun Route.registerCategoriesRoutes(
    eventDao: EventDao,
    categoryDao: CategoryDao
) {
    val repository = CategoryRepository(eventDao, categoryDao)

    get("/categories") {
        val eventId = call.parameters["eventId"]!!
        call.respond(HttpStatusCode.OK, repository.list(eventId))
    }
    get("/categories/{id}") {
        val eventId = call.parameters["eventId"]!!
        val catId = call.parameters["id"]!!
        call.respond(HttpStatusCode.OK, repository.get(eventId, catId))
    }
    post("/categories") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val catInput = call.receiveValidated<CategoryInput>()
        call.respond(HttpStatusCode.Created, repository.create(eventId, apiKey, catInput))
    }
    put("/categories/{id}") {
        val eventId = call.parameters["eventId"]!!
        val apiKey = call.request.headers["api_key"]!!
        val catId = call.parameters["id"]!!
        val catInput = call.receiveValidated<CategoryInput>()
        call.respond(HttpStatusCode.OK, repository.update(eventId, apiKey, catId, catInput))
    }
}
