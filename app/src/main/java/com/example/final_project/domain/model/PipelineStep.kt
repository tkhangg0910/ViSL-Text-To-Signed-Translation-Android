package com.example.final_project.domain.model

enum class StepStatus {
    WAITING,

    RUNNING,

    DONE,

    ERROR,
}

data class PipelineStep(
    val index:  Int,
    val name:   String,
    val status: StepStatus,
)
