package com.example.final_project.data.remote.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
sealed class SseEvent {
    data class StepStart(
        val step: Int,
        val name: String,
    ) : SseEvent()

    data class StepDone(
        val step: Int,
        val name: String,
        val result: JsonElement?,
    ) : SseEvent()

    data class StepError(
        val step: Int,
        val name: String,
        val error: String,
    ) : SseEvent()

    data class Complete(
        val videoB64: String,
        val gloss: GlossDto,
        val tokens: List<String>,
    ) : SseEvent()

    data class Unknown(val rawEvent: String, val rawData: String) : SseEvent()

}