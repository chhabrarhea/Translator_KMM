package com.rhea.translator.domain.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

actual class HttpClientFactory {
    actual fun create() = HttpClient(Darwin) {
        install(ContentNegotiation) {
            json()
        }
    }
}