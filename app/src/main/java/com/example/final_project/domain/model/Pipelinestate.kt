package com.example.final_project.domain.model

sealed class PipelineState {

    data object Idle : PipelineState()

    data class Loading(
        val steps: List<PipelineStep>,
    ) : PipelineState()

    data class Success(
        val result: TranslationResult,
    ) : PipelineState()

    data class Error(
        val message: String,
        val steps:   List<PipelineStep> = emptyList(),
    ) : PipelineState()
}

