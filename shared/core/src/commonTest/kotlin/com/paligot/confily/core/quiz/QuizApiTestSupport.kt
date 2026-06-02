package com.paligot.confily.core.quiz

import com.paligot.confily.core.api.ConferenceApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private val jsonHeaders = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())

class RecordingResponder {
    val requestedPaths = mutableListOf<String>()
    val requestBodies = mutableListOf<String>()
    val routes = mutableMapOf<String, Pair<HttpStatusCode, String>>()

    fun on(pathSuffix: String, status: HttpStatusCode = HttpStatusCode.OK, body: String = "") {
        routes[pathSuffix] = status to body
    }
}

fun conferenceApiWith(responder: RecordingResponder): ConferenceApi {
    val engine = MockEngine { request ->
        responder.requestedPaths.add(request.url.encodedPath)
        responder.requestBodies.add((request.body as? io.ktor.http.content.TextContent)?.text ?: "")
        val match = responder.routes.entries.firstOrNull { request.url.encodedPath.endsWith(it.key) }
            ?: error("No mock route for ${request.url.encodedPath}")
        respond(content = match.value.second, status = match.value.first, headers = jsonHeaders)
    }
    val client = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json { isLenient = true; ignoreUnknownKeys = true })
        }
    }
    return ConferenceApi(client = client, baseUrl = "https://test.local")
}
