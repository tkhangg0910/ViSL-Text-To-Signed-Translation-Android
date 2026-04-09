package com.example.final_project.data.remote.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompletePayloadDto(
    @SerialName("video_b64")
    val videoB64: String,

    @SerialName("video_mime")
    val videoMime: String = "video/mp4",

    @SerialName("gloss")
    val gloss: GlossDto,

    @SerialName("tokens")
    val tokens: List<String>,
)
