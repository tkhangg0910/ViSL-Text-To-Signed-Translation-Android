package com.example.final_project.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class StepStartDto(
    @SerialName("step")
    val step: Int,

    @SerialName("name")
    val name: String,
)

@Serializable
data class StepDoneDto(
    @SerialName("step")
    val step: Int,

    @SerialName("name")
    val name: String,

    @SerialName("result")
    val result: JsonElement? = null,
)

@Serializable
data class StepErrorDto(
    @SerialName("step")
    val step: Int,

    @SerialName("name")
    val name: String,

    @SerialName("error")
    val error: String,
)
