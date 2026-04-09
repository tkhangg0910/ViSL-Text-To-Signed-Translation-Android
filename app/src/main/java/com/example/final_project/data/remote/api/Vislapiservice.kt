package com.example.final_project.data.remote.api


import android.util.Log
import com.example.final_project.data.remote.model.CompletePayloadDto
import com.example.final_project.data.remote.model.SseEvent
import com.example.final_project.data.remote.model.StepDoneDto
import com.example.final_project.data.remote.model.StepErrorDto
import com.example.final_project.data.remote.model.StepStartDto
import com.example.final_project.data.remote.model.TranslateRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "ViSLApiService"
private const val TRANSLATE_ENDPOINT = "/api/v1/translate/stream"

@Singleton
class ViSLApiService @Inject constructor(
    private val httpClient: HttpClient,
    private val json: Json,
) {

    fun translateStream(request: TranslateRequestDto): Flow<SseEvent> = flow {
        httpClient.sse(
            urlString = TRANSLATE_ENDPOINT,
            request = {
                method = HttpMethod.Post
                setBody(request)
            },
        ) {
            incoming.collect { serverSentEvent ->
                val eventType = serverSentEvent.event ?: run {
                    Log.v(TAG, "SSE event without type field, skipping")
                    return@collect
                }
                val data = serverSentEvent.data ?: ""

                Log.d(TAG, "SSE ← [$eventType] ${data.take(120)}")

                emit(parseEvent(eventType, data))
            }
        }
    }

    // Parser
    private fun parseEvent(eventType: String, data: String): SseEvent =
        runCatching {
            when (eventType) {
                "step_start" -> {
                    val dto = json.decodeFromString<StepStartDto>(data)
                    SseEvent.StepStart(step = dto.step, name = dto.name)
                }
                "step_done" -> {
                    val dto = json.decodeFromString<StepDoneDto>(data)
                    SseEvent.StepDone(step = dto.step, name = dto.name, result = dto.result)
                }
                "step_error" -> {
                    val dto = json.decodeFromString<StepErrorDto>(data)
                    SseEvent.StepError(step = dto.step, name = dto.name, error = dto.error)
                }
                "complete" -> {
                    val dto = json.decodeFromString<CompletePayloadDto>(data)
                    SseEvent.Complete(
                        videoB64 = dto.videoB64,
                        gloss    = dto.gloss,
                        tokens   = dto.tokens,
                    )
                }
                else -> {
                    Log.w(TAG, "Unknown SSE event type: $eventType")
                    SseEvent.Unknown(rawEvent = eventType, rawData = data)
                }
            }
        }.getOrElse { throwable ->
            Log.e(TAG, "Parse failed [$eventType]: $data", throwable)
            SseEvent.Unknown(rawEvent = eventType, rawData = data)
        }
}

