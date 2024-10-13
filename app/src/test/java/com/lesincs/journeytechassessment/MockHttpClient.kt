package com.lesincs.journeytechassessment

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.append
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> getMockHttpClient(
    path: String,
    success: Boolean,
    responseBody: T? = null,
) = HttpClient(MockEngine) {
    engine {
        addHandler { request ->
            if (path == request.url.encodedPath && success) {
                respond(
                    Json.encodeToString(responseBody),
                    HttpStatusCode.OK,
                    headers {
                        append(HttpHeaders.ContentType, ContentType.Application.Json)
                    }
                )
            } else {
                error("Error response for ${request.url.encodedPath}")
            }
        }
    }
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    defaultRequest {
        contentType(ContentType.Application.Json)
        accept(ContentType.Application.Json)
        url("https://jsonplaceholder.typicode.com")
    }
}
