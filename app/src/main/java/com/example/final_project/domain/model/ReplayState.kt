package com.example.final_project.domain.model

data class ReplayState(
    val uriPath: String?,
    val glossTokens: List<GlossToken>,
    val dialect: Dialect,
    val inputText: String,
)
