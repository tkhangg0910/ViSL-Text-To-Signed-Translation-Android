package com.example.final_project.di
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.sse.SSE
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = " https://taxation-reasonable-george-performs.trycloudflare.com/"

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideHttpClient(json: Json): HttpClient = HttpClient(OkHttp) {

        // ── Content Negotiation ──────────────────────────────────────────────
        install(ContentNegotiation) {
            json(json)
        }

        install(SSE)

        // ── Timeout ──────────────────────────────────────────────────────────
        install(HttpTimeout) {
            connectTimeoutMillis = 15_000L
            requestTimeoutMillis = Long.MAX_VALUE
            socketTimeoutMillis  = 60_000L
        }

        // Default Request
        defaultRequest {
            url(BASE_URL)
            contentType(ContentType.Application.Json)
        }

        // Logging
        install(Logging) {
            level = LogLevel.HEADERS
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("KtorClient", message)
                }
            }
        }
    }
}
