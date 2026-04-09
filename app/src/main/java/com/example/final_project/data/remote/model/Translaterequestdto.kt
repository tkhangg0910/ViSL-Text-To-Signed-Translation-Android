package com.example.final_project.data.remote.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TranslateRequestDto(
    @SerialName("text")
    val text: String,

    @SerialName("dialect")
    val dialect: String,

    @SerialName("top_k")
    val topK: Int = 5,
)